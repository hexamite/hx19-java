fixed = [
    50: [x: 1000, y: 1000, z:0],
    51: [x: 2140, y: 1000, z:0],
    52: [x: 1000, y: 2140, z:0]
]

diffx50_51 = fixed[51].x - fixed[50].x // 1140
diffy50_52 = fixed[52].y - fixed[50].y // 1140

distances = [
    50: 3217,
    51: 3158,
    52: 3073
]

locusRx = distances[50]
Rxx = distances[51]
Ryy = distances[52]

println "$locusRx, $Rxx, $Ryy"

coordinates = trilaterate(locusRx, Rxx, diffx50_51, Ryy, diffy50_52)

println("coordinates: $coordinates")


def trilaterate(locusRx, Rxx, diffx50_51, Ryy, diffy50_52) {

    p = [:]

    p.x = 0.5 * (locusRx ** 2 - Rxx ** 2 + diffx50_51 ** 2) / diffx50_51
    p.y = 0.5 * (locusRx ** 2 - Ryy ** 2 + diffy50_52 ** 2) / diffy50_52
    p.z = Math.sqrt(locusRx ** 2 - xsquare ** 2 - ysquare ** 2)

    println "relativeDistance xyz $p"

    p.x += 1000
    p.y += 1000

    return p

}
