//
//  rsa.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 10/05/23.
//

import Foundation

class RSA {
    private let n: Int
    private let e: Int
    private let d: Int

    init(e: Int) throws {
        var p = Int.random(in: 100..<1000)
        var q = Int.random(in: 100..<1000)
        while isComposite(p, iterations: 10) || isComposite(q, iterations: 10) || p == q {
            p = Int.random(in: 100..<1000)
            q = Int.random(in: 100..<1000)
        }
        n = p * q
        let phi = (p - 1) * (q - 1)
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

    var publicKey: (e: Int, n: Int) {
        return (e, n)
    }

    func encrypt(_ m: Int, otherPublicKey: (e: Int, n: Int)) throws -> Int {
        if m >= otherPublicKey.n {
            throw RSAError.mTooBig
        }
        return power(m, otherPublicKey.e, modulo: otherPublicKey.n)
    }

    func decrypt(_ c: Int) -> Int {
        return power(c, d, modulo: n)
    }
}

enum RSAError: Error {
    case notCoprime
    case eTooBig
    case noInverse
    case mTooBig
}