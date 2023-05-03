//
//  gcd.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 14/04/23.
//

import Foundation

func gcd(_ a: Int, _ b: Int) -> Int {
    var r: Int
    var a = a
    var b = b
    while b != 0 {
        r = a % b
        a = b
        b = r
    }
    return a
}

func extendedGCD(_ a: Int, _ b: Int) -> (Int, Int, Int) {
    var s = 0
    var oldS = 1
    var r = b
    var oldR = a
    while r != 0 {
        let quotient = oldR / r
        (oldR, r) = (r, oldR - quotient * r)
        (oldS, s) = (s, oldS - quotient * s)
    }
    var bezoutT: Int
    if b != 0 {
        bezoutT = (oldR - oldS * a) / b
    } else {
        bezoutT = 0
    }
    return (oldR, oldS, bezoutT)
}

func inverse(_ a: Int, _ n: Int) -> Int? {
    let output = extendedGCD(a, n)
    if output.0 == 1 {
        if output.1 < 0 {
            return output.1 + n
        } else {
            return output.1
        }
    } else {
        return nil
    }
}