import binascii
"""
Author: Nathan Belcher
"""

initial_vector = 6161616161616161 #used for xor with first block in hash

#converts the message to a hex notation and casts to string type
def to_str_hex(string):
    bytes = string.encode()
    hex_val = binascii.hexlify(bytes).decode('ascii')
    int_val = int(hex_val, 16)
    str_val = str(int_val)
    print(str_val)
    return str_val

#takes in hex notation of message and splits it into blocks of 32 bits
def string_split(hex):
    sections = []
    int_sections = []
    max_32 = ""
    for char in hex:
        if len(max_32) < 16:
            max_32 += char
        if len(max_32) == 16:
            sections.append(max_32)
            max_32 = ""
    if len(max_32) == 0:
        return int_sections
    while len(max_32) < 16:
        max_32 += '0'
    sections.append(max_32)
    for i in sections:
        intval = int(i)
        int_sections.append(intval)
    return int_sections

#performs the xor calculation of the split blocks to create 32 bit hash
def xor_calc(list):
    result = 0
    previous = ""
    for i in list:
        if previous == "":
            result = (initial_vector ^ i)
            print(result)
            previous = result
        else:
            result = hex(int(previous ^ i))
            previous = result
    return result

def hash(message):
    hexval = to_str_hex(message)
    list = string_split(hexval)
    bytes = xor_calc(list)
    return bytes

def main():
    print(hash('hellothere'))
main()

