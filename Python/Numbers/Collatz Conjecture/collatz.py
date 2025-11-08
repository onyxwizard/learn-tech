def steps(number):
        number = check_valid(number)
        step_count = 0
        while number!=1:
            if number%2 ==0:
                number =  even(number)
            else:
                    number = odd(number)
            step_count +=1
        return step_count
    
def check_valid(number):
    if number >0:
        return number
    else:
        raise ValueError("Only positive integers are allowed")
        
def even(number):
    number = number//2
    return number

def odd(number):
    number= (3*number)+1
    return number
