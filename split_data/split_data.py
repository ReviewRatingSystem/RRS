import json
import sys
import pprint
from collections import defaultdict
pp = pprint.PrettyPrinter(indent=4)

# Get input
if len(sys.argv) == 4:
    yelp_business_dir = sys.argv[1]
    yelp_review_dir = sys.argv[2]
    out_dir = sys.argv[3]
else:
    print "Usage: python split_data.py business_data review_data out_directory"
    sys.exit()

def stringToFilename(s):
    return  "".join(x for x in s if x.isalnum())

# Make map from business_id to category
yelp_business_file = open(yelp_business_dir)

BidToCategory = {}

for line in yelp_business_file:
    b_json = json.loads(line)
    BidToCategory[b_json["business_id"]] = b_json["categories"]

yelp_business_file.close()

print "Read Businesses"

# Make dictionary of category to [review]
yelp_review_file = open(yelp_review_dir)

CatToReview = defaultdict(list)

count = 0
limit = 1569264
for line in yelp_review_file:
    count += 1
    if count > limit:
        break
    else:
        print str(count/float(limit)) + "%"
    r_json = json.loads(line)
    for cat in BidToCategory[r_json["business_id"]]:
        CatToReview[cat].append(r_json)

print "Read Categories"

# Output data
for category, reviews in CatToReview.items():
    filename = out_dir + stringToFilename(category) + '.json'
    f = open(filename, 'w+')
    for r in reviews:
        f.write(json.dumps(r) + '\n')
    print "Created", filename
    f.close()
