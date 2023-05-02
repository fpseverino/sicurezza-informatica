//
//  miller-rabin.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 02/05/23.
//

import Foundation

var n: Int
var iter: Int

if CommandLine.argc != 3 {
    print("Usage: gcd <n> <iterations>")
    exit(1)
}

if let arg1 = Int(CommandLine.arguments[1]) {
    if arg1 > 2 && (arg1 % 2) == 1 {
        n = arg1
    } else {
        print("ERROR: n must be greater than 2 and odd")
        exit(1)
    }
} else {
    print("ERROR: First argument is not a number")
    exit(1)
}

if let arg2 = Int(CommandLine.arguments[2]) {
    iter = arg2
} else {
    print("ERROR: Second argument is not a number")
    exit(1)
}

for _ in 0..<iter {
    if test(n) {
        print("composite")
        exit(0)
    }
}
print("probably prime")
exit(0)

func test(_ n: Int) -> Bool {
    var N = n - 1
    var k = 0
    while (N % 2) == 0 {
        N /= 2
        k += 1
    }
    let q = (n - 1) / power(2, k)
    let a = Int.random(in: 2..<(n - 1))
    if exponentiation(a, q, n: n) == 1 {
        return false
    }
    for j in 0..<k {
        if exponentiation(a, power(2, j) * q, n: n) == (n - 1) {
            return false
        }
    }
    return true
}

func power(_ b: Int, _ e: Int) -> Int {
    Int(truncating: NSDecimalNumber(decimal: pow(Decimal(b), e)))
}

func exponentiation(_ a: Int, _ b: Int, n: Int) -> Int {
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