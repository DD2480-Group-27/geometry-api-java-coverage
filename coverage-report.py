import tempfile
import os

# Define the hardcoded file path
file_path = tempfile.gettempdir() + "/diy-coverage/overlaps_/log"

file_contents = ""

# Open the file in read mode
try:
    with open(file_path, 'r') as file:
        # Read the contents of the file
        file_contents = file.read()
        # Print the contents of the file
except FileNotFoundError:
    print("No report has been found make sure to run the test again before each report.")
    exit(1)
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    # Delete the file
    if os.path.exists(file_path):
        os.remove(file_path)

def count_numbers(file_content):
    # Initialize an array of size 6 with zeros
    count_array = [0] * 9

    # Split the string by newline characters and iterate through each number
    for line in file_content.split('\n'):
        if line.isdigit():  # Check if the line is a digit
            number = int(line)
            if 0 <= number <= 8:  # Ensure the number is within the valid range
                count_array[number] += 1

    return count_array


result = count_numbers(file_contents)

print("      \033[1mCoverage result:\033[0m")
print("   // Checks whether the scl string is the overlaps relation.")
print("   private static boolean overlaps_(String scl, int dim_a, int dim_b) {")
print(f"      Branch taken \033[1m{result[0]}\033[0m times")
print("      if (dim_a == dim_b) {")
print(f"         Branch taken \033[1m{result[1]}\033[0m times")
print("         if (dim_a != 1) {")
print(f"            Branch taken \033[1m{result[2]}\033[0m times")
print("            // Valid for area-area, Point-Point")
print("            if (scl.charAt(0) == 'T' && scl.charAt(1) == '*'")
print("                  && scl.charAt(2) == 'T' && scl.charAt(3) == '*'")
print("                  && scl.charAt(4) == '*' && scl.charAt(5) == '*'")
print("                  && scl.charAt(6) == 'T' && scl.charAt(7) == '*'")
print("                  && scl.charAt(8) == '*') {")
print(f"               Branch taken \033[1m{result[3]}\033[0m times")
print("               return true;")
print("            }")
print(f"            Branch taken \033[1m{result[4]}\033[0m times")
print("            return false;")
print("         }")
print(f"         Branch taken \033[1m{result[5]}\033[0m times")
print("         // Valid for Line-Line")
print("         if (scl.charAt(0) == '1' && scl.charAt(1) == '*'")
print("               && scl.charAt(2) == 'T' && scl.charAt(3) == '*'")
print("               && scl.charAt(4) == '*' && scl.charAt(5) == '*'")
print("               && scl.charAt(6) == 'T' && scl.charAt(7) == '*'")
print("               && scl.charAt(8) == '*') {")
print(f"            Branch taken \033[1m{result[6]}\033[0m times")
print("            return true;")
print("         }")
print(f"         Branch taken \033[1m{result[7]}\033[0m times")
print("      }")
print(f"      Branch taken \033[1m{result[8]}\033[0m times")
print("      return false;")
print("   }")

