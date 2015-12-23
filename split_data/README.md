# Examples

## Sort data into categories
`python split_data.py yelp_academic_dataset_business.json yelp_academic_dataset_review.json ./reviews/`

Takes the yelp business and review data and outputs a list of files named after categories which contain one review per line into the directory `./reviews/`

## Extract random lines
`python extract_lines.py reviews/Restaurants.json samples/ 10000 1000 1000`

Takes in the Restaurants reviews and outputs into 10000 random lines into `./samples/train.json`, 1000 random lines into `./samples/dev.json` and 1000 random lines into `./samples/test.json`.

## Run
Example to produce split data for Restaurants:
```
python split_data.py yelp_academic_dataset_business.json yelp_academic_dataset_review.json ./reviews/
python extract_lines.py reviews/Restaurants.json ./samples/ 10000 1000 1000
```
