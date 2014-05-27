package com.hexamite.util

/**
 *
 */
class Evaluate {

    def closure

    Evaluate(code) {
        def shell = new GroovyShell()
        closure = shell.evaluate("{->$code}")
    }

    def eval(delegate) {
        closure.delegate = delegate
        closure()
    }

    def eval() {
        closure()
    }

}
