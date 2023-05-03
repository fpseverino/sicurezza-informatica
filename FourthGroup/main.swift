//
//  main.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

print("Do you want to use the Miller-Rabin test or the Chinese Remainder Theorem? (miller-rabin/ctr)")
let input = readLine()!

if input == "mr" {
    print("How many iterations?")
    let iterations = Int(readLine()!)!
    print("What number do you want to test?")
    let number = Int(readLine()!)!
    print(isComposite(number, iterations: iterations) ? "Composite" : "Prime")
} else if input == "ctr" {
    print("Insert the first array of numbers:")
    let num = readLine()!.split(separator: " ").map { Int($0)! }
    print("Insert the second array of numbers:")
    let rem = readLine()!.split(separator: " ").map { Int($0)! }
    print(ctr(num: num, rem: rem)!)
} else {
    print("Invalid input")
}