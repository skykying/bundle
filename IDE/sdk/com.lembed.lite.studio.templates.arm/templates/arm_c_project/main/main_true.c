/*----------------------------------------------------------------------------
 *      main Template for ARM C/C++ Project
 *----------------------------------------------------------------------------
 *      Name:    main.c
 *      Purpose: Generic main program body including main() function
 *      Rev.:    1.0.0
 *----------------------------------------------------------------------------*/
#include "stdlib.h"
#include "stdint.h"


#include "components.h"

#ifdef ENABLE_GCOV
#include <libgcov.h>
#endif

#ifdef ENABLE_GPROF
#include <gmon.h>
#endif

/* main function */
int main(void)
{
#ifdef ENABLE_GPROF
	_monInit();
#endif

#ifdef ENABLE_GCOV
	static_init();
#endif

	//TODO: add system initialize code here;

	/* Infinite loop */
	while (1) {

		//TODO: Add application code here

	}

#ifdef ENABLE_GPROF
	_mcleanup(); /* write gmon.out file */
#endif
}

