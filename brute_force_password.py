import hashlib
import bcrypt


if __name__ == "__main__":
    target_password = input("Password to check: ").strip()
    common_passwords = [
        "password",
        "123456",
        "qwerty",
        "abc123",
        "password1",
        "letmein",
        "welcome",
        "admin",
        "monkey",
        "123456789"
    ] 

    if target_password in common_passwords:
        print(f"'{target_password}' is a common password and vulnerable to brute-force attacks.")
    else:
        print(f"'{target_password}' is not in the common list.")

    target_hash = input("Enter a bcrypt hash to try to crack (or press enter to skip): ").strip()
    if target_hash:
        cracked = False
        for pwd in common_passwords:
            try:
                if bcrypt.checkpw(pwd.encode('utf-8'), target_hash.encode('utf-8')):
                    print(f"Cracked! The password is '{pwd}'")
                    cracked = True
                    break
            except ValueError:
                continue
        if not cracked:
            print("Could not crack the hash with the common list.")

    sha256_hash = input("Enter a SHA-256 hash to try to crack (or press enter to skip): ").strip()
    if sha256_hash:
        cracked = False
        for pwd in common_passwords:
            hashed = hashlib.sha256(pwd.encode('utf-8')).hexdigest()
            if hashed == sha256_hash:
                print(f"Cracked! The password is '{pwd}'")
                cracked = True
                break
        if not cracked:
            print("Could not crack the hash with the common list.")


'''
    Summary:
    Weak or common passwords can be easily cracked through brute-force or dictionary attacks,
especially when stored using fast, unsalted hashes like SHA-256.
    To stay secure, passwords must be long, unique, and complex, and developers should store
them using salted, slow hashing algorithms such as bcrypt.
    Additional protection measures—like two-factor authentication, password managers and
regular password updates—further reduce the risk of breaches. Educating users and following proper
security practices is essential for keeping accounts and data safe.

--- $2b$12$D60gK28j.aqTZTiSS0cp0O.K6gcNNc.y37Nvz7K6Eeo9MO1jWr9Je
--- 65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5
'''