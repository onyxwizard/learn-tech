def response(hey_bob):
    # Normalize the input by stripping leading/trailing whitespace
    hey_bob = hey_bob.strip()
    
    # Check for silence
    if not hey_bob:
        return "Fine. Be that way!"
    
    # Check if the input is yelling (all caps) and contains at least one letter
    is_yelling = hey_bob.isupper()  # True if all letters are uppercase and there's at least one letter
    
    # Check if the input is a question (ends with '?')
    is_question = hey_bob.endswith('?')
    
    # Determine Bob's response based on the conditions
    if is_yelling and is_question:
        return "Calm down, I know what I'm doing!"
    elif is_yelling:
        return "Whoa, chill out!"
    elif is_question:
        return "Sure."
    else:
        return "Whatever."