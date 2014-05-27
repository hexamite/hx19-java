package com.hexamite.trilaterate

import javax.inject.Inject
import java.util.logging.Logger

/**
 * Parses a stream of distance measurements and calls `consumer` after each block of measurements.
 * A block is a sequence of measurements from different receivers to the same transmitter for the
 * same ultrasound pulse.
 */
class Parser {

    Logger logger = Logger.getLogger(Parser.name)

    private static final def DISTANCE_PATTERN = /^R(\d+) P(\d+)_(\d+) A(\d+)$/    // R21 P31_453 A1345

    private def distances = [:]  // receiver : distance
    private def key // transmitter and block-id identify the block

    Closure consume

    def parse(String line) {
        assert consume
        boolean match

        (line =~ DISTANCE_PATTERN).find { _, receiver, transmitter, block, distance ->
            if([transmitter, block] != key) {
                if (distances.size() >= 3) {
                    consume(transmitter as int, distances)
                } else if(key) {
                    logger.info "Missing distances $key $distances"
                }
                distances = [:]
                key = [transmitter, block]
            }
            distances[receiver as int] = (distance as int)
            match = true
        }

        if(!match) {
            logger.info  "Unreckognized '$line"
        }

    }
}


