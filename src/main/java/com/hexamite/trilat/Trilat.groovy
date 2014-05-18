package com.hexamite.trilat

/**
 * Trilaterates a point from distances to three known positions.
 */
class Trilat {

    /**
     * Trilaterates a point from distances to three known positions.
     */
    def trilaterate(List fixed, List dists) {
        def origin = fixed[0]
        def norms = normalize(origin, fixed)
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
