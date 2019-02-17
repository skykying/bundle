
/*******************************************************************************

 *         customised "exit, _sbrk, assert" functions override newlib nano
 *
 *******************************************************************************/

#include <sys/types.h>

#include <stdlib.h>
#include <errno.h>
#include <assert.h>
#include <stdint.h>

#include "Trace/Trace.h"


/*******************************************************************************
 *          override _exit of newlib nano
 *******************************************************************************/

#if !defined(DEBUG)
extern void __attribute__((noreturn)) __reset_hardware(void);
#endif

// ----------------------------------------------------------------------------

// Forward declaration
void _exit(int code);

// On Release, call the hardware reset procedure.
// On Debug we just enter an infinite loop, to be used as landmark when halting
// the debugger.
//
// It can be redefined in the application, if more functionality
// is required.

void __attribute__((weak)) _exit(int code __attribute__((unused)))
{
#if !defined(DEBUG)
  __reset_hardware();
#endif

  // TODO: write on trace
  while (1) ;
}

// ----------------------------------------------------------------------------

void __attribute__((weak, noreturn)) abort(void)
{
  trace_puts("abort(), exiting...");
  _exit(1);
}

/*******************************************************************************
 *            override _sbrk of newlib nano
 *******************************************************************************/

caddr_t _sbrk(int incr);

// The definitions used here should be kept in sync with the
// stack definitions in the linker script.

caddr_t _sbrk(int incr)
{
  extern char _Heap_Begin; // Defined by the linker.
  extern char _Heap_Limit; // Defined by the linker.

  static char* current_heap_end;
  char* current_block_address;

  if (current_heap_end == 0) {
    current_heap_end = &_Heap_Begin;
  }

  current_block_address = current_heap_end;

  // Need to align heap to word boundary, else will get
  // hard faults on Cortex-M0. So we assume that heap starts on
  // word boundary, hence make sure we always add a multiple of
  // 4 to it.
  incr = (incr + 3) & (~3); // align value to 4
  if (current_heap_end + incr > &_Heap_Limit) {
    // Some of the libstdc++-v3 tests rely upon detecting
    // out of memory errors, so do not abort here.
#if 0
    extern void abort (void);

    _write (1, "_sbrk: Heap and stack collision\n", 32);

    abort ();
#else
    // Heap has overflowed
    errno = ENOMEM;
    return (caddr_t) - 1;
#endif
  }

  current_heap_end += incr;
  return (caddr_t) current_block_address;
}

/*******************************************************************************
 *            override _write of newlib nano
 *******************************************************************************/
// Do not include on semihosting and when freestanding
#if !defined(OS_USE_SEMIHOSTING) && !(__STDC_HOSTED__ == 0)

// ----------------------------------------------------------------------------

// When using retargetted configurations, the standard write() system call,
// after a long way inside newlib, finally calls this implementation function.

// Based on the file descriptor, it can send arrays of characters to
// different physical devices.

// Currently only the output and error file descriptors are tested,
// and the characters are forwarded to the trace device, mainly
// for demonstration purposes. Adjust it for your specific needs.

// For freestanding applications this file is not used and can be safely
// ignored.

ssize_t _write (int fd, const char* buf, size_t nbyte);

ssize_t _write (int fd __attribute__((unused)),
                const char* buf __attribute__((unused)),
                size_t nbyte __attribute__((unused)))
{
#if defined(TRACE)
  // STDOUT and STDERR are routed to the trace device
  if (fd == 1 || fd == 2) {
    return trace_write (buf, nbyte);
  }
#endif // TRACE

  errno = ENOSYS;
  return -1;
}

// ----------------------------------------------------------------------------

#endif // !defined(OS_USE_SEMIHOSTING) && !(__STDC_HOSTED__ == 0)


/*******************************************************************************
 *            override _write of newlib nano
 *******************************************************************************/
void __attribute__((noreturn)) __assert_func (const char *file,
    int line,
    const char *func,
    const char *failedexpr)
{
  trace_printf ("assertion \"%s\" failed: file \"%s\", line %d%s%s\n",
                failedexpr, file, line, func ? ", function: " : "",
                func ? func : "");
  abort ();
  /* NOTREACHED */
}

// This is STM32 specific, but can be used on other platforms too.
// If you need it, add the following to your application header:

//#ifdef  USE_FULL_ASSERT
//#define assert_param(expr) ((expr) ? (void)0 : assert_failed((uint8_t *)__FILE__, __LINE__))
//void assert_failed(uint8_t* file, uint32_t line);
//#else
//#define assert_param(expr) ((void)0)
//#endif // USE_FULL_ASSERT

#if defined(USE_FULL_ASSERT)

void assert_failed (uint8_t* file, uint32_t line);

// Called from the assert_param() macro, usually defined in the stm32f*_conf.h
void __attribute__((noreturn, weak)) assert_failed (uint8_t* file, uint32_t line)
{
  trace_printf ("assert_param() failed: file \"%s\", line %d\n", file, line);
  abort ();
  /* NOTREACHED */
}

#endif // defined(USE_FULL_ASSERT)

/*******************************************************************************
 *            implement stack protector functions for newlib nano
 *******************************************************************************/

void __stack_chk_fail (void);

void __attribute__((noreturn, weak)) __stack_chk_fail (void)
{
  abort ();
}

void __stack_chk_guard (void);

void __attribute__((noreturn, weak)) __stack_chk_guard (void)
{
  abort ();
}

/*******************************************************************************
 *            implement for -finstrument-functions option of gcc
 *******************************************************************************/

void __attribute__((__no_instrument_function__))
__cyg_profile_func_enter(__attribute__((unused))void *this_func, __attribute__((unused))void *call_site)
{

}
void __attribute__((__no_instrument_function__))
__cyg_profile_func_exit(__attribute__((unused))void *this_func, __attribute__((unused))void *call_site)
{

}