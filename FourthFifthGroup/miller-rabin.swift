//
//  miller-rabin.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 02/05/23.
//

import Foundation

func test(_ n: Int) -> Bool {
    var N = n - 1
    var k = 0
    while (N % 2) == 0 {
        N /= 2
        k += 1
    }
    let q = (n - 1) / power(2, k)
    let a = Int.random(in: 2..<(n - 1))
    if exponentiation(a, q, modulo: n) == 1 {
        return false
    }
    for j in 0..<k {
        if exponentiation(a, power(2, j) * q, modulo: n) == (n - 1) {
            return false
        }
    }
    return true
}

func isComposite(_ n: Int, iterations: Int) -> Bool {
    for _ in 0..<iterations {
        if test(n) {
            return true
        }
    }
    return false
}

func power(_ b: Int, _ e: Int) -> Int {
    Int(truncating: NSDecimalNumber(decimal: pow(Decimal(b), e)))
}

func exponentiation(_ a: Int, _ b: Int, modulo n: Int) -> Int {
    var f = 1
    let bin = String(b, radix: 2)
    for i in bin {
        f = (f * f) % n
        if i == "1" {
            f = (f * a) % n
        }
    }
    return f
}