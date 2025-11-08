import os
def print_file_contents(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            print(f"\n=== Contents of {os.path.basename(file_path)} ===\n")
            print(file.read())
            print(f"\n=== End of {os.path.basename(file_path)} ===\n")
    except Exception as e:
        print(f"Error reading file '{file_path}': {e}")
        
def print_all_files_in_folder(folder_path, extension=None):
    if not os.path.isdir(folder_path):
        print(f"Error: '{folder_path}' is not a valid directory.")
        return
    
    for filename in os.listdir(folder_path):
        file_path = os.path.join(folder_path,filename)
        if os.path.isfile(file_path):
            if extension is None or filename.endswith(extension):
                print_file_contents(file_path)
            
if __name__ == "__main__":
    folder = input("enter folder path: ").strip()
    file_type = input("Enter the file extension to filter").strip()
    print_all_files_in_folder(folder,file_type if file_type else None)