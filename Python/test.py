# import os
# from pathlib import Path

# # Define the directory path
# arr = [
#     "Section_26_Multithreading",
#     "Section_27_Thread_Synchronization_Techniques",
#     "Section_28_Sharing_Data_Between_Threads",
#     "Section_29_Multiprocessing",
#     "Section_30_Async_IO"
# ]
# dir_path = os.getcwd()
# # Check if the directory exists
# for dir in arr:
#     path = Path(dir)
#     if not path.is_dir():
#     # If not, create it
#         os.mkdir(dir)
#     print(f"Directory '{dir}' created.")
# else:
#     print(f"Directory '{dir}' already exists.")
import os

base = os.getcwd()
folder = [file for file in os.listdir(base) if os.path.isdir(file)]
folder.pop(0)
n = len(folder)
c =0
for dir in folder:
    cur_path = f'{base}\{dir}\\README.md'
    alt_path = f'{base}\{dir}\\'
    if not os.path.isfile(cur_path):
        with open(cur_path,'x') as f:
            print(f'File Created {cur_path}')
