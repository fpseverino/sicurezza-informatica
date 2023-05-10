//
//  rsa.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 10/05/23.
//

import Foundation

class RSA {
    let p: Int
    let q: Int
    let n: Int
    let phi: Int
    let e: Int
    let d: Int

    init(p: Int, q: Int, e: Int) throws {
        if isComposite(p, iterations: 10) || isComposite(q, iterations: 10) {
            throw RSAError.notPrime
        }
        if p == q {
            throw RSAError.samePrimes
        }
        self.p = p
        self.q = q
        n = p * q
        phi = (p - 1) * (q - 1)
        if gcd(e, phi) != 1 {
            throw RSAError.notCoprime
        }
        if e >= phi {
            throw RSAError.eTooBig
        }
        self.e = e
        if let d = inverse(e, modulo: phi) {
            self.d = d
        } else {
            throw RSAError.noInverse
        }
    }

    func encrypt(_ m: Int) throws -> Int {
        if m >= n {
            throw RSAError.mTooBig
        }
        return power(m, e, modulo: n)
    }

    func decrypt(_ c: Int) -> Int {
        return power(c, d, modulo: n)
    }
}

enum RSAError: Error {
    case notPrime
    case samePrimes
    case notCoprime
    case eTooBig
    case noInverse
    case mTooBig
}