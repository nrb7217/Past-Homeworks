import hw8
import string

def brute_force():
    #hash_list = []
    hash_dict = {}
    for c1 in string.ascii_lowercase:
        for c2 in string.ascii_lowercase:
            for c3 in string.ascii_lowercase:
                for c4 in string.ascii_lowercase:
                    for c5 in string.ascii_lowercase:
                        for c6 in string.ascii_lowercase:
                            for c7 in string.ascii_lowercase:
                                for c8 in string.ascii_lowercase:
                                    result = print(hw8.hash(c1+c2+c3+c4))
                                    if result in hash_dict:
                                        print("collision found! " + (c1+c2+c3+c4+c5+c6+c7+c8) + " " + hash_dict[result])
                                        break
                                    hash_dict[result] = (c1+c2+c3+c4+c5+c6+c7+c8)
    print(hash_dict)
brute_force()