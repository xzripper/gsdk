# Simple utility for replacing keyword(s) in directory files / single file.
# Example: python kwdrepl.py [FILE/DIRECTORY] [OLD (REPLACE~ME)] [NEW (REPLACED~YOU)] (OPTIONAL: --log);

from os import walk
from os.path import join, isfile, isdir

from argparse import ArgumentParser

def replace_in_file(file_path: str, keyword_pairs: list, log=False) -> None:
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    with open(file_path, 'w', encoding='utf-8') as file:
        for line_number, line in enumerate(lines):
            for old, new in keyword_pairs:
                while (start_pos := line.find(old, start_pos)) != -1:
                    line = line[:start_pos] + new + line[start_pos + len(old):]

                    if log:
                        print(f'{file_path}:{line_number+1}:{start_pos}: replaced "{old}" with "{new}".')

            file.write(line)

def replace_in_directory(directory: str, keyword_pairs: list, log=False) -> None:
    for root, _, files in walk(directory):
        for file in files:
            replace_in_file(join(root, file), keyword_pairs, log)

parser = ArgumentParser(description='Replace keywords in files within a directory or a single file.')

parser.add_argument('path', type=str, help='The file or directory to search.')
parser.add_argument('keywords', type=str, help='Keywords to replace, separated by ~ (tilda).')
parser.add_argument('replacements', type=str, help='Replacements, separated by ~ (tilda).')

parser.add_argument('--log', action='store_true', help='Log after replacing keyword.')

args = parser.parse_args()

keywords = args.keywords.split('~')
replacements = args.replacements.split('~')

if len(keywords) != len(replacements):
    print('ERROR: Invalid number of keywords and replacements (should be equal).'); exit(1)

keyword_pairs = list(zip(keywords, replacements))

if isfile(args.path): replace_in_file(args.path, keyword_pairs, args.log)
elif isdir(args.path): replace_in_directory(args.path, keyword_pairs, args.log)
else: print('ERROR: Invalid target: expected file or directory.'); exit(1)
