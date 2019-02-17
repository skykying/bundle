#ifdef ENABLE_GPROF

#include "gprof.h"

#include <stdio.h>
#include <sys/types.h>
#include <errno.h>
#include <math.h>
#include <string.h>
#include <stdint.h>

/* global profinfo for profil() call */
static struct profinfo prof = {
  PROFILE_NOT_INIT, 0, 0, 0, 0
};

/* sample the current program counter */
void ProfTick_Handler(void) {
  //void OSA_SysTick_Handler(void);
  static size_t pc, idx;

  /* http://blog.csdn.net/gujing001/article/details/6882363 */
  if (prof.state==PROFILE_ON) {
    pc = ((uint32_t*)(__builtin_frame_address(0)))[14]; /* get SP and use it to get the return address from stack */
    if (pc >= prof.lowpc && pc < prof.highpc) {
      idx = PROFIDX (pc, prof.lowpc, prof.scale);
      prof.counter[idx]++;
    }
  }
}

/* Stop profiling to the profiling buffer pointed to by p. */
static int profile_off (struct profinfo *p) {
  p->state = PROFILE_OFF;
  return 0;
}

/* Create a timer thread and pass it a pointer P to the profiling buffer. */
static int profile_on (struct profinfo *p) {
  p->state = PROFILE_ON;
  return 0; /* ok */
}

/*
 * start or stop profiling
 *
 * profiling goes into the SAMPLES buffer of size SIZE (which is treated
 * as an array of u_shorts of size size/2)
 *
 * each bin represents a range of pc addresses from OFFSET.  The number
 * of pc addresses in a bin depends on SCALE.  (A scale of 65536 maps
 * each bin to two addresses, A scale of 32768 maps each bin to 4 addresses,
 * a scale of 1 maps each bin to 128k address).  Scale may be 1 - 65536,
 * or zero to turn off profiling
 */
int profile_ctl (struct profinfo *p, char *samples, size_t size, size_t offset, u_int scale) {
  size_t maxbin;

  if (scale > 65536) {
    errno = EINVAL;
    return -1;
  }
  profile_off(p);
  if (scale) {
    memset(samples, 0, size);
    memset(p, 0, sizeof *p);
    maxbin = size >> 1;
    prof.counter = (u_short*)samples;
    prof.lowpc = offset;
    prof.highpc = PROFADDR(maxbin, offset, scale);
    prof.scale = scale;
    return profile_on(p);
  }
  return 0;
}

/* Equivalent to unix profil()
   Every SLEEPTIME interval, the user's program counter (PC) is examined:
   offset is subtracted and the result is multiplied by scale.
   The word pointed to by this address is incremented. */
int profil (char *samples, size_t size, size_t offset, u_int scale) {
  return profile_ctl (&prof, samples, size, offset, scale);
}

extern void __gnu_mcount_nc(void);

/**
 * arm v7 m assembly
 */
void __gnu_mcount_nc( void )
{
#if defined(ARMCM7)
	__asm volatile (
#if 0 /* dummy version, doing nothing */
			" mov    ip, lr \n"
			" pop    { lr } \n"
			" bx     ip \n"
#else
			"	push {r0, r1, r2, r3, lr}     \n"/* save registers */
			"	bic r1, lr, #1                \n"/* R1 contains callee address, with thumb bit cleared */
			"	ldr r0, [sp, #20]             \n"/* R0 contains caller address */
			"	bic r0, r0, #1                \n"/* clear thumb bit */
			"	bl _mcount_internal           \n"/* jump to internal _mcount() implementation */
			"	pop {r0, r1, r2, r3, ip, lr}  \n"/* restore saved registers */
			"	bx ip							\n"
#endif
	);
#endif
}

#endif
