package com.hexamite.trilaterate

import org.jglue.cdiunit.CdiRunner
import org.junit.*
import org.junit.runner.RunWith

import javax.inject.Inject

@RunWith(CdiRunner)
class TrilateratorTest {

    @Inject Trilaterator trilaterator

    @Test
    void testTrilaterate() {

        // Distance of the unknown point to the fixed points

        def dists = [
                3217,
                3158,
                3073
        ]

        assert trilaterator.trilaterate(dists) == new Point(1734, 1967, 2979)

    }
}
