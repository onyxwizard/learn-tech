def equilateral(sides):
    a, b, c = sides
    if is_valid_triangle(sides) and a == b == c:
        return True
    return False


def isosceles(sides):
    a, b, c = sides
    if is_valid_triangle(sides) and (a == b or b == c or a == c):
        return True
    return False


def scalene(sides):
    a, b, c = sides
    if is_valid_triangle(sides) and a != b and b != c and a != c:
        return True
    return False


def is_valid_triangle(sides):
    # Validate the triangle inequality theorem
    a, b, c = sorted(sides)  # Sort sides to simplify checks
    return a > 0 and b > 0 and c > 0 and (a + b > c)