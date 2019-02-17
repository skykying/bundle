/* profil.h: gprof profiling header file

   Copyright 1998, 1999, 2000, 2001, 2002 Red Hat, Inc.

This file is part of Cygwin.

This software is a copyrighted work licensed under the terms of the
Cygwin license.  Please consult the file "CYGWIN_LICENSE" for
details. */

/*
 * This file is taken from Cygwin distribution. Please keep it in sync.
 * The differences should be within __MINGW32__ guard.
 */

#ifndef __PROFIL_H__
#define __PROFIL_H__

#ifdef ENABLE_GPROF

#ifndef __u_char_defined

/* Type definitions for BSD code. */
typedef unsigned long  u_long;
typedef unsigned int   u_int;
typedef unsigned short u_short;
typedef unsigned char  u_char;
typedef unsigned int   size_t;

#endif

#ifndef __P
#define __P(x) x
#endif

/* profiling frequency.  (No larger than 1000) */
#define PROF_HZ			1000

/* convert an addr to an index */
#define PROFIDX(pc, base, scale)	\
  ({									\
    size_t i = (pc - base) / 2;				\
    if (sizeof (unsigned long long int) > sizeof (size_t))		\
      i = (unsigned long long int) i * scale / 65536;			\
    else								\
      i = i / 65536 * scale + i % 65536 * scale / 65536;		\
    i;									\
  })

/* convert an index into an address */
#define PROFADDR(idx, base, scale)		\
  ((base)					\
   + ((((unsigned long long)(idx) << 16)	\
       / (unsigned long long)(scale)) << 1))

/* convert a bin size into a scale */
#define PROFSCALE(range, bins)		(((bins) << 16) / ((range) >> 1))

typedef void *_WINHANDLE;

typedef enum {
  PROFILE_NOT_INIT = 0,
  PROFILE_ON,
  PROFILE_OFF
} PROFILE_State;

struct profinfo {
  PROFILE_State state; /* profiling state */
  u_short *counter;			/* profiling counters */
  size_t lowpc, highpc;		/* range to be profiled */
  u_int scale;			/* scale value of bins */
};

int profile_ctl(struct profinfo *, char *, size_t, size_t, u_int);
int profil(char *, size_t, size_t, u_int);

#endif

#ifdef ENABLE_GPROF
/* On POSIX systems, profile.h is a KRB5 header.  To avoid collisions, just
   pull in profile.h's content here.  The profile.h header won't be provided
   by Mingw-w64 anymore at one point. */

#ifndef _WIN64
#define _MCOUNT_CALL __attribute__ ((regparm (2)))
extern void _mcount(void);
#else
#define _MCOUNT_CALL
extern void mcount(void);
#endif

#define _MCOUNT_DECL __attribute__((gnu_inline)) __inline__ \
   void _MCOUNT_CALL _mcount_private
#define MCOUNT



/*
 * Structure prepended to gmon.out profiling data file.
 */
struct gmonhdr {
	size_t	lpc;		/* base pc address of sample buffer */
	size_t	hpc;		/* max pc address of sampled buffer */
	int	ncnt;		    /* size of sample buffer (plus this header) */
	int	version;	  /* version number */
	int	profrate;	  /* profiling clock rate */
	int	spare[3];	  /* reserved */
};
#define GMONVERSION	0x00051879

/*
 * histogram counters are unsigned shorts (according to the kernel).
 */
#define	HISTCOUNTER	unsigned short

/*
 * fraction of text space to allocate for histogram counters here, 1/2
 */
#define	HISTFRACTION	2

/*
 * Fraction of text space to allocate for from hash buckets.
 * The value of HASHFRACTION is based on the minimum number of bytes
 * of separation between two subroutine call points in the object code.
 * Given MIN_SUBR_SEPARATION bytes of separation the value of
 * HASHFRACTION is calculated as:
 *
 *	HASHFRACTION = MIN_SUBR_SEPARATION / (2 * sizeof(short) - 1);
 *
 * For example, on the VAX, the shortest two call sequence is:
 *
 *	calls	$0,(r0)
 *	calls	$0,(r0)
 *
 * which is separated by only three bytes, thus HASHFRACTION is
 * calculated as:
 *
 *	HASHFRACTION = 3 / (2 * 2 - 1) = 1
 *
 * Note that the division above rounds down, thus if MIN_SUBR_FRACTION
 * is less than three, this algorithm will not work!
 *
 * In practice, however, call instructions are rarely at a minimal
 * distance.  Hence, we will define HASHFRACTION to be 2 across all
 * architectures.  This saves a reasonable amount of space for
 * profiling data structures without (in practice) sacrificing
 * any granularity.
 */
#define	HASHFRACTION	2

/*
 * percent of text space to allocate for tostructs with a minimum.
 */
#define ARCDENSITY	2 /* this is in percentage, relative to text size! */
#define MINARCS		 50
#define MAXARCS		 ((1 << (8 * sizeof(HISTCOUNTER))) - 2)

struct tostruct {
	size_t	selfpc; /* callee address/program counter. The caller address is in froms[] array which points to tos[] array */
	long	count;    /* how many times it has been called */
	u_short	link;   /* link to next entry in hash table. For tos[0] this points to the last used entry */
	u_short pad;    /* additional padding bytes, to have entries 4byte aligned */
};

/*
 * a raw arc, with pointers to the calling site and
 * the called site and a count.
 */
struct rawarc {
	size_t	raw_frompc;
	size_t	raw_selfpc;
	long	raw_count;
};

/*
 * general rounding functions.
 */
#define ROUNDDOWN(x,y)	(((x)/(y))*(y))
#define ROUNDUP(x,y)	  ((((x)+(y)-1)/(y))*(y))

/*
 * The profiling data structures are housed in this structure.
 */
struct gmonparam {
	int		state;
	u_short		*kcount;    /* histogram PC sample array */
	size_t		kcountsize; /* size of kcount[] array in bytes */
	u_short		*froms;     /* array of hashed 'from' addresses. The 16bit value is an index into the tos[] array */
	size_t		fromssize;  /* size of froms[] array in bytes */
	struct tostruct	*tos; /* to struct, contains histogram counter */
	size_t		tossize;    /* size of tos[] array in bytes */
	long		  tolimit;
	size_t		lowpc;      /* low program counter of area */
	size_t		highpc;     /* high program counter */
	size_t		textsize;   /* code size */
};
extern struct gmonparam _gmonparam;

/*
 * Possible states of profiling.
 */
#define	GMON_PROF_ON	  0
#define	GMON_PROF_BUSY	1
#define	GMON_PROF_ERROR	2
#define	GMON_PROF_OFF	  3

void _mcleanup(void); /* routine to be called to write gmon.out file */
void _monInit(void); /* initialization routine */

#endif
#endif /* __PROFIL_H__ */
