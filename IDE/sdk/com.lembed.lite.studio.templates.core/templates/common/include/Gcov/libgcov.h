/* Header file for libgcov-*.c.
 Copyright (C) 1996-2015 Free Software Foundation, Inc.

 This file is part of GCC.

 GCC is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the Free
 Software Foundation; either version 3, or (at your option) any later
 version.

 GCC is distributed in the hope that it will be useful, but WITHOUT ANY
 WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 for more details.

 Under Section 7 of GPL version 3, you are granted additional
 permissions described in the GCC Runtime Library Exception, version
 3.1, as published by the Free Software Foundation.

 You should have received a copy of the GNU General Public License and
 a copy of the GCC Runtime Library Exception along with this program;
 see the files COPYING3 and COPYING.RUNTIME respectively.  If not, see
 <http://www.gnu.org/licenses/>.  */

#ifndef __LIBGCOV_H
#define __LIBGCOV_H

#ifdef __cplusplus
extern "C"{
#endif

#ifdef ENABLE_GCOV

/* work around the poisoned malloc/calloc in system.h.  */
#ifndef xmalloc
#define xmalloc malloc
#endif
#ifndef xcalloc
#define xcalloc calloc
#endif

#ifndef IN_GCOV_TOOL

#if BITS_PER_UNIT == 8
typedef unsigned gcov_unsigned_t __attribute__ ((mode (SI)));
typedef unsigned gcov_position_t __attribute__ ((mode (SI)));
#if LONG_LONG_TYPE_SIZE > 32
typedef signed gcov_type __attribute__ ((mode (DI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (DI)));
#else
typedef signed gcov_type __attribute__ ((mode (SI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (SI)));
#endif
#else
#if BITS_PER_UNIT == 16
typedef unsigned gcov_unsigned_t __attribute__ ((mode (HI)));
typedef unsigned gcov_position_t __attribute__ ((mode (HI)));
#if LONG_LONG_TYPE_SIZE > 32
typedef signed gcov_type __attribute__ ((mode (SI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (SI)));
#else
typedef signed gcov_type __attribute__ ((mode (HI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (HI)));
#endif
#else
typedef unsigned gcov_unsigned_t __attribute__ ((mode (QI)));
typedef unsigned gcov_position_t __attribute__ ((mode (QI)));
#if LONG_LONG_TYPE_SIZE > 32
typedef signed gcov_type __attribute__ ((mode (HI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (HI)));
#else
typedef signed gcov_type __attribute__ ((mode (QI)));
typedef unsigned gcov_type_unsigned __attribute__ ((mode (QI)));
#endif
#endif
#endif

#if defined (TARGET_POSIX_IO)
#define GCOV_LOCKED 1
#else
#define GCOV_LOCKED 0
#endif

/* In libgcov we need these functions to be extern, so prefix them with
 __gcov.  In libgcov they must also be hidden so that the instance in
 the executable is not also used in a DSO.  */
#define gcov_var __gcov_var__ported
#define gcov_open __gcov_open__ported
#define gcov_close __gcov_close__ported
#define gcov_write_tag_length __gcov_write_tag_length__ported
#define gcov_seek __gcov_seek__ported
#define gcov_write_unsigned __gcov_write_unsigned__ported
#define gcov_write_counter __gcov_write_counter__ported
#define gcov_write_summary __gcov_write_summary__ported
#define gcov_sort_n_vals __gcov_sort_n_vals__ported
#define gcov_exit gcov_exit__ported
#define __gcov_dump_one __gcov_dump_one__ported

/* @STRIPPED lines 105-136 */

#endif /* !IN_GCOV_TOOL */

/* @STRIPPED lines 140-151 */

#ifdef HAVE_GAS_HIDDEN
#define ATTRIBUTE_HIDDEN  __attribute__ ((__visibility__ ("hidden")))
#else
#define ATTRIBUTE_HIDDEN
#endif

//////////////////////////////////////////////////////////////////////////////////////////////////////
#ifdef ENABLE_GCOV

/* @STRIPPED lines 167-192 */

#ifndef GCOV_LINKAGE
#define GCOV_LINKAGE extern
#endif

#if IN_LIBGCOV
#define gcov_nonruntime_assert(EXPR) ((void)(0 && (EXPR)))
#else
#define gcov_nonruntime_assert(EXPR) gcc_assert (EXPR)
#define gcov_error(...) fatal_error (input_location, __VA_ARGS__)
#endif

/* File suffixes.  */
//#define GCOV_DATA_SUFFIX ".gcda"
//#define GCOV_NOTE_SUFFIX ".gcno"

/* File magic. Must not be palindromes.  */
#define GCOV_DATA_MAGIC ((gcov_unsigned_t)0x67636461) /* "gcda" */
#define GCOV_NOTE_MAGIC ((gcov_unsigned_t)0x67636e6f) /* "gcno" */

/*
 * We did not have any gcov-iov.h generated which contains the version.
 * Figured with gcov complaints about an invalid version in our manually
 * generated GCDA versions. The version that gcov from this version of
 * GNU ARM gcc needs is '504r' (0x35303472), and so what could have been in
 * gcov-iov.h is directly mentioned here
 */
#define GCOV_VERSION ((gcov_unsigned_t)0x35303472)

/* Convert a magic or version number to a 4 character string.  */
#define GCOV_UNSIGNED2STRING(ARRAY,VALUE) \
  ((ARRAY)[0] = (char)((VALUE) >> 24),    \
   (ARRAY)[1] = (char)((VALUE) >> 16),    \
   (ARRAY)[2] = (char)((VALUE) >> 8),   \
   (ARRAY)[3] = (char)((VALUE) >> 0))

/* The record tags.  Values [1..3f] are for tags which may be in either
 file.  Values [41..9f] for those in the note file and [a1..ff] for
 the data file.  The tag value zero is used as an explicit end of
 file marker -- it is not required to be present.  */

#define GCOV_TAG_FUNCTION    ((gcov_unsigned_t)0x01000000)
#define GCOV_TAG_FUNCTION_LENGTH (3)
#define GCOV_TAG_BLOCKS      ((gcov_unsigned_t)0x01410000)
#define GCOV_TAG_BLOCKS_LENGTH(NUM) (NUM)
#define GCOV_TAG_BLOCKS_NUM(LENGTH) (LENGTH)
#define GCOV_TAG_ARCS        ((gcov_unsigned_t)0x01430000)
#define GCOV_TAG_ARCS_LENGTH(NUM)  (1 + (NUM) * 2)
#define GCOV_TAG_ARCS_NUM(LENGTH)  (((LENGTH) - 1) / 2)
#define GCOV_TAG_LINES       ((gcov_unsigned_t)0x01450000)
#define GCOV_TAG_COUNTER_BASE    ((gcov_unsigned_t)0x01a10000)
#define GCOV_TAG_COUNTER_LENGTH(NUM) ((NUM) * 2)
#define GCOV_TAG_COUNTER_NUM(LENGTH) ((LENGTH) / 2)
#define GCOV_TAG_OBJECT_SUMMARY  ((gcov_unsigned_t)0xa1000000) /* Obsolete */
#define GCOV_TAG_PROGRAM_SUMMARY ((gcov_unsigned_t)0xa3000000)
#define GCOV_TAG_SUMMARY_LENGTH(NUM)  (1 + GCOV_COUNTERS_SUMMABLE * (10 + 3 * 2) + (NUM) * 5)
#define GCOV_TAG_AFDO_FILE_NAMES ((gcov_unsigned_t)0xaa000000)
#define GCOV_TAG_AFDO_FUNCTION ((gcov_unsigned_t)0xac000000)
#define GCOV_TAG_AFDO_WORKING_SET ((gcov_unsigned_t)0xaf000000)

/* Counters that are collected.  */

enum {
	/* Arc transitions.  */
	GCOV_COUNTER_ARCS,

	/* Histogram of value inside an interval.  */
	GCOV_COUNTER_V_INTERVAL,

	/* Histogram of exact power2 logarithm of a value.  */
	GCOV_COUNTER_V_POW2,

	/* The most common value of expression.  */
	GCOV_COUNTER_V_SINGLE,

	/* The most common difference between consecutive values of expression.  */
	GCOV_COUNTER_V_DELTA,

	/* The most common indirect address.  */
	GCOV_COUNTER_V_INDIR,

	/* Compute average value passed to the counter.  */
	GCOV_COUNTER_AVERAGE,

	/* IOR of the all values passed to counter.  */
	GCOV_COUNTER_IOR,

	/* Time profile collecting first run of a function */
	GCOV_TIME_PROFILER,

	/* Top N value tracking for indirect calls.  */
	GCOV_COUNTER_ICALL_TOPNV,

	/* All */
	GCOV_COUNTERS
};

/* Counters which can be summaried.  */
#define GCOV_COUNTERS_SUMMABLE  (GCOV_COUNTER_ARCS + 1)

/* The first of counters used for value profiling.  They must form a
 consecutive interval and their order must match the order of
 HIST_TYPEs in value-prof.h.  */
#define GCOV_FIRST_VALUE_COUNTER GCOV_COUNTERS_SUMMABLE

/* The last of counters used for value profiling.  */
#define GCOV_LAST_VALUE_COUNTER (GCOV_COUNTERS - 1)

/* Number of counters used for value profiling.  */
#define GCOV_N_VALUE_COUNTERS \
  (GCOV_LAST_VALUE_COUNTER - GCOV_FIRST_VALUE_COUNTER + 1)

/* The number of hottest callees to be tracked.  */
#define GCOV_ICALL_TOPN_VAL  2

/* The number of counter entries per icall callsite.  */
#define GCOV_ICALL_TOPN_NCOUNTS (1 + GCOV_ICALL_TOPN_VAL * 4)

/* Convert a counter index to a tag.  */
#define GCOV_TAG_FOR_COUNTER(COUNT)             \
    (GCOV_TAG_COUNTER_BASE + ((gcov_unsigned_t)(COUNT) << 17))
/* Convert a tag to a counter.  */
#define GCOV_COUNTER_FOR_TAG(TAG)                   \
    ((unsigned)(((TAG) - GCOV_TAG_COUNTER_BASE) >> 17))
/* Check whether a tag is a counter tag.  */
#define GCOV_TAG_IS_COUNTER(TAG)                \
    (!((TAG) & 0xFFFF) && GCOV_COUNTER_FOR_TAG (TAG) < GCOV_COUNTERS)

/* The tag level mask has 1's in the position of the inner levels, &
 the lsb of the current level, and zero on the current and outer
 levels.  */
#define GCOV_TAG_MASK(TAG) (((TAG) - 1) ^ (TAG))

/* Return nonzero if SUB is an immediate subtag of TAG.  */
#define GCOV_TAG_IS_SUBTAG(TAG,SUB)             \
    (GCOV_TAG_MASK (TAG) >> 8 == GCOV_TAG_MASK (SUB)    \
     && !(((SUB) ^ (TAG)) & ~GCOV_TAG_MASK (TAG)))

/* Return nonzero if SUB is at a sublevel to TAG.  */
#define GCOV_TAG_IS_SUBLEVEL(TAG,SUB)               \
        (GCOV_TAG_MASK (TAG) > GCOV_TAG_MASK (SUB))

/* Basic block flags.  */
#define GCOV_BLOCK_UNEXPECTED   (1 << 1)

/* Arc flags.  */
#define GCOV_ARC_ON_TREE    (1 << 0)
#define GCOV_ARC_FAKE       (1 << 1)
#define GCOV_ARC_FALLTHROUGH    (1 << 2)

/* Structured records.  */

/* Structure used for each bucket of the log2 histogram of counter values.  */
typedef struct {
	/* Number of counters whose profile count falls within the bucket.  */
	gcov_unsigned_t num_counters;
	/* Smallest profile count included in this bucket.  */
	gcov_type min_value;
	/* Cumulative value of the profile counts in this bucket.  */
	gcov_type cum_value;
} gcov_bucket_type;

/* For a log2 scale histogram with each range split into 4
 linear sub-ranges, there will be at most 64 (max gcov_type bit size) - 1 log2
 ranges since the lowest 2 log2 values share the lowest 4 linear
 sub-range (values 0 - 3).  This is 252 total entries (63*4).  */

#define GCOV_HISTOGRAM_SIZE 252

/* How many unsigned ints are required to hold a bit vector of non-zero
 histogram entries when the histogram is written to the gcov file.
 This is essentially a ceiling divide by 32 bits.  */
#define GCOV_HISTOGRAM_BITVECTOR_SIZE (GCOV_HISTOGRAM_SIZE + 31) / 32

/* Cumulative counter data.  */
struct gcov_ctr_summary {
	gcov_unsigned_t num; /* number of counters.  */
	gcov_unsigned_t runs; /* number of program runs */
	gcov_type sum_all; /* sum of all counters accumulated.  */
	gcov_type run_max; /* maximum value on a single run.  */
	gcov_type sum_max; /* sum of individual run max values.  */
	gcov_bucket_type histogram[GCOV_HISTOGRAM_SIZE]; /* histogram of
	 counter values.  */
};

/* Object & program summary record.  */
struct gcov_summary {
	gcov_unsigned_t checksum; /* checksum of program */
	struct gcov_ctr_summary ctrs[GCOV_COUNTERS_SUMMABLE];
};

/* Functions for reading and writing gcov files. In libgcov you can
 open the file for reading then writing. Elsewhere you can open the
 file either for reading or for writing. When reading a file you may
 use the gcov_read_* functions, gcov_sync, gcov_position, &
 gcov_error. When writing a file you may use the gcov_write
 functions, gcov_seek & gcov_error. When a file is to be rewritten
 you use the functions for reading, then gcov_rewrite then the
 functions for writing.  Your file may become corrupted if you break
 these invariants.  */

/*
 * This section of gcov-io.h is heavily changed in our libgcov port.
 * The function prototypes mentioned here are those used outside
 * gcov-io.c, in libgcov-driver.c and libgcov-driver-system.c. Since we
 * no longer follow the model of "including" gcov-io.c in our port, we
 * have made these prototypes available to the 2 C files.
 */

GCOV_LINKAGE int gcov_open(const char */*name*/) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE int gcov_close(void) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE void gcov_seek(gcov_position_t base) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE void gcov_write_unsigned(gcov_unsigned_t) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE void gcov_write_counter(gcov_type value) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE void gcov_write_tag_length(gcov_unsigned_t tag, gcov_unsigned_t length) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE void gcov_write_summary(gcov_unsigned_t tag, const struct gcov_summary *summary) ATTRIBUTE_HIDDEN;
GCOV_LINKAGE unsigned gcov_histo_index(gcov_type value);
GCOV_LINKAGE void gcov_histogram_merge(gcov_bucket_type *tgt_histo,	gcov_bucket_type *src_histo);

#endif
//////////////////////////////////////////////////////////////////////////////////////////////////


/* Structures embedded in coveraged program.  The structures generated
 by write_profile must match these.  */

/* Information about counters for a single function.  */
struct gcov_ctr_info {
	gcov_unsigned_t num; /* number of counters.  */
	gcov_type *values; /* their values.  */
};

/* Information about a single function.  This uses the trailing array
 idiom. The number of counters is determined from the merge pointer
 array in gcov_info.  The key is used to detect which of a set of
 comdat functions was selected -- it points to the gcov_info object
 of the object file containing the selected comdat function.  */

struct gcov_fn_info {
	const struct gcov_info *key; /* comdat key */
	gcov_unsigned_t ident; /* unique ident of function */
	gcov_unsigned_t lineno_checksum; /* function lineo_checksum */
	gcov_unsigned_t cfg_checksum; /* function cfg checksum */
	struct gcov_ctr_info ctrs[1]; /* instrumented counters */
};

/* Type of function used to merge counters.  */
typedef void (*gcov_merge_fn)(gcov_type *, gcov_unsigned_t);

/* Information about a single object file.  */
struct gcov_info {
	gcov_unsigned_t version; /* expected version number */
	struct gcov_info *next; /* link to next, used by libgcov */

	gcov_unsigned_t stamp; /* uniquifying time stamp */
	const char *filename; /* output file name */

	gcov_merge_fn merge[GCOV_COUNTERS]; /* merge functions (null for
	 unused) */

	unsigned n_functions; /* number of functions */

#ifndef IN_GCOV_TOOL
	const struct gcov_fn_info * const *functions; /* pointer to pointers
	 to function information  */
#else
	const struct gcov_fn_info **functions;
#endif /* !IN_GCOV_TOOL */
};

/* Root of a program/shared-object state */
struct gcov_root {
	struct gcov_info *list;
	unsigned dumped :1; /* counts have been dumped.  */
	unsigned run_counted :1; /* run has been accounted for.  */
	struct gcov_root *next;
	struct gcov_root *prev;
};

extern struct gcov_root __gcov_root ATTRIBUTE_HIDDEN;

struct gcov_master {
	gcov_unsigned_t version;
	struct gcov_root *root;
};

/* Exactly one of these will be active in the process.  */
extern struct gcov_master __gcov_master;

/* Dump a set of gcov objects.  */
extern void __gcov_dump_one(struct gcov_root *) ATTRIBUTE_HIDDEN;

/* @STRIPPED lines 235-344 */

#endif

#ifdef ENABLE_GCOV

/* This preprocessor directive is manually defined here. */
#define IN_LIBGCOV              1

/*
 * IMPORTANT
 * Do not define this!
 */
/*
 #define IN_GCOV                 1
 */

/*
 * This is defined by GCC sources as 8 and should not need any change
 * for most platforms. This value works when compiling with GNU ARM
 * tool chain for the test project where this port was verified.
 */
#define BITS_PER_UNIT           8

/*
 * This is defined for each platform/architecture in GCC code. The value
 * for most architectures is 64. The aarch64.h file by ARM which is part
 * of GCC defines it as 64. The GNU ARM Eclipse test project that we used
 * uses Aarch32 tool chain and not Aarch64; however, sizeof(long)
 * returns 64, so we went with 64 as the value.
 */
#define LONG_LONG_TYPE_SIZE     64

/*
 * IMPORTANT
 * Do not define this!
 *
 * TARGET_POSIX_IO is defined only for standard OSes like Linux in GCC.
 * This is a very important macro as GCOV_LOCKED macro gets different
 * values based on whether this is defined or not.
 *
 * I made our decision of not defining this macro as follows:
 *   - Our port of libgcov uses no file handling (POSIX IO)
 */
/*
 #define TARGET_POSIX_IO
 */

/*
 * This is copied from gcc/system.h as opposed to bringing that file here
 * as this was the only macro needed from that file
 */

#if ENABLE_ASSERT_CHECKING
#define gcc_assert(EXPR)                        \
   ((void)(!(EXPR) ? fancy_abort (__FILE__, __LINE__, __FUNCTION__), 0 : 0))
#elif (GCC_VERSION >= 4005)
#define gcc_assert(EXPR)                        \
  ((void)(__builtin_expect (!(EXPR), 0) ? __builtin_unreachable (), 0 : 0))
#else
/* Include EXPR, so that unused variable warnings do not occur.  */
#define gcc_assert(EXPR) ((void)(0 && (EXPR)))
#endif

void gcov_init(void);
void gcov_exit(void);

#endif

#ifdef __cplusplus
}
#endif

#endif /* __LIBGCOV_H */
