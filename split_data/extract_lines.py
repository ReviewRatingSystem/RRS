import random,  sys

# Get input
if len(sys.argv) == 6:
    infile = sys.argv[1]
    outdir = sys.argv[2]
    train_count = int(sys.argv[3])
    dev_count = int(sys.argv[4])
    test_count = int(sys.argv[5])
else:
    print "Usage: python extract_lines.py infile outfile train_count dev_count test_count"
    sys.exit()

with open(infile, "rb") as source:
    lines = [line for line in source]

random_choice = random.sample(lines,  train_count + dev_count + test_count)

with open(outdir + 'train.json', "wb") as sink:
    sink.write("".join(random_choice[:train_count]))

with open(outdir + 'dev.json', "wb") as sink:
    sink.write("".join(random_choice[train_count:train_count+dev_count]))

with open(outdir + 'test.json', "wb") as sink:
    sink.write("".join(random_choice[train_count+dev_count:]))
