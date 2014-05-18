// The locations of the fixed points

fixed = [
    new Point(1000, 1000, 0),
    new Point(2140, 1000, 0),
    new Point(1000, 2140, 0)
]

// Translate the fixed points to "normalized" coordinate system

norm = [
    fixed[0] - fixed[0],  // 0,0,0
    fixed[1] - fixed[0],  // 1140, 0, 0
    fixed[2] - fixed[0]   // 0, 1140, 0
]

// Distance of the unknown point to the fixed points

dists = [
    3217,
    3158,
    3073
]

// Calculate location of unknown point in normalized coordinate system

p = trilaterate(norm, dists)

//  Reverse the normalization

p = p + fixed[0]

println p // prints Point(1734, 1967, 2979)

// Calculate location of a point with know distances to 3 points in a "normalized"
// coordinate system
def trilaterate(norms, dists) {

    p = new Point()

    p.x = 0.5 * (dists[0] ** 2 - dists[1] ** 2 + norms[1].x ** 2) / norms[1].x  // Shouldn't that be '/ ( 2 * norms[1]) ?
    p.y = 0.5 * (dists[0] ** 2 - dists[2] ** 2 + norms[2].y ** 2) / norms[2].y  // Shouldn't that be '/ ( 2 * norms[2]) ?
    p.z = Math.sqrt(dists[0] ** 2 - p.x ** 2 - p.y ** 2)

    return p
}


class Point {
    int x, y, z
    Point() { }
    Point(x, y, z) { this.x = x; this.y = y; this.z = z }
    def plus(Point p) { new Point(x + p.x, y + p.y, z + p.z) }
    def minus(Point p) { new Point(x - p.x, y - p.y, z - p.z) }
    String toString() { "Point($x, $y, $z)" }
}