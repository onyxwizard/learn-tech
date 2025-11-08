def is_armstrong_number(number):
    if number < 10:
        return True
    test = number
    count = 0
    
    # Count the number of digits
    while test > 0:
        test //= 10
        count += 1
    
    # Reset test and calculate the sum of powers
    test = number
    calc = 0
    while test > 0:
        calc += (test % 10) ** count
        test //= 10
    
    # Return True if the number is an Armstrong number
    return calc == number