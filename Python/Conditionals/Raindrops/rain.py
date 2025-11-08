import operator
def convert(number):
    #take a empty string
    result = " "
    
    if operator.mod(number,3) == 0:
        result+="Pling"
    if operator.mod(number,5) == 0:
        result+="Plang"
    if operator.mod(number,7) == 0:
        result+="Plong"
    
    return result if result else str(number)