package com.hexamite.trilaterate

import com.hexamite.cdi.config.Config
import com.hexamite.cdi.config.Script

import javax.inject.Inject

/**
 * Trilaterates a point from distances to three known positions.
 */
class Trilaterator {

    private List<Point> norms // The normalized coordinates of the fixed points in the system
    private Point origin // The point in the fixed system that will move to the origin in the normalized system

    /**
     * @param fixed The coordinates of the fixed points in the system.
     * */
    @Inject @Config @Script(file='example/conf/fixedPoints.groovy') List<Point> fixed

    def Trilaterator() {
        origin = fixed[0]
        norms = normalize(origin, fixed)
    }

    /**
     * Trilaterates a point from distances to three known positions.
     */
    def trilaterate(List dists) {
        def p = trilaterateNormalized(norms, dists)
        return denormalize(origin, p)
    }

    /**
     * Trilaterates a point from distances to three known positions in a "normalized"
     * coordinate system.
     */
    private def trilaterateNormalized(List norms, List dists) {

        def p = new Point()

        p.x = 0.5 * (dists[0] ** 2 - dists[1] ** 2 + norms[1].x ** 2) / norms[1].x  // Shouldn't that be '/ ( 2 * norms[1]) ?
        p.y = 0.5 * (dists[0] ** 2 - dists[2] ** 2 + norms[2].y ** 2) / norms[2].y  // Shouldn't that be '/ ( 2 * norms[2]) ?
        p.z = Math.sqrt(dists[0] ** 2 - p.x ** 2 - p.y ** 2)

        return p
    }

    private def normalize(Point origin, List points) {
        points.collect { normalize(origin, it) }
    }

    private def normalize(Point origin, Point point) {
        point - origin
    }

    private def denormalize(Point origin, Point point) {
        point + origin
    }
}
