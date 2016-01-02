# RRS
Review Rating System

Reviews are an important source of information for businesses looking for ways to provide better service as well as for customers trying to weigh their options. However, with the rise in popularity of applications like Yelp or Foursquare, the amount of available information quickly becomes overwhelming and prevents users from utilizing it efficiently. The Review Rating System (RRS) addresses this problem by providing a numerical estimate of ratings (on a 1-5 scale) when such information is absent and extracts the most important elements of a review (such as tips or other words that justify the rating). The RRS uses the Yelp reviews for training and testing.

##Related Work

We found two major papers written on this topic.

Researchers at Stanford published a paper called “Predicting Star Ratings of Movie Review Comments”. They used Naive Bayes and SVM as classifier types. They used unigrams and  bigrams with a Naive Bayes classifier, and achieved an overall accuracy of 52%.

AT&T Labs researchers also published a paper called “Capturing the Stars: Predicting Ratings for Service and Product Reviews”. They used a classifier type called Binary one-vs-all Maximum Entropy (MaxEnt). They used unigrams, bigrams, and word chunks for feature types. As a result, their MaxEnt method only achieved an accuracy of 55%

#Approach

## Data Pre-processing
Data processing is a step that prepares an input review in a way that makes it ready for classification, either in the training phase to build the models, or the testing phase in the run-time of the application. Below are the different steps the data pre-processing involves.

1. White-Space Tokenization: The text of the review is white-space tokenized. This makes all the digits and punctuation marks separate from the preceding and following words.
2. Sentence Splitting: The text is then split into sentences. This is necessary to ensure no N-grams span across different sentences.
3. Lemmatization: All the words are converted to their Lemma. The lemma is the original form of a word that bears the actual meaning without any inflections. Working on lemmas instead of actual words makes the learning models less sparse, especially we are not interested in the morphological differences that result from the change in tense or number, for instance. Additionally, all proper nouns are converted to a constant string "PROP_NOUN", which makes the learning models less sparse as well. We use the StanfordCoreNLP package for lemmatization.
4. Part-of-Speech Tagging: All words are tagged for their Part-of-Speech (POS). In any further processing, only nouns, adjectives, adverbs, verbs and interjections are processed. This is because only those POS tags bear a meaning..We use the StanfordCoreNLP package for POS tagging.


## Sentiment Annotation
All the relevant words generated after pre-processing are tagged for their positive and negative scores. The scores are generated from English SentiWordNet, version 3.0. The lookup is performed using the lemma and POS information generated using the method above. In the case where there is more than a possible match, the average scores of the matching entries is assigned. Additionally, a neutral sentiment of zero positive and negative scores is assigned in the cases where no match exists. If a word is preceded by a
word that denotes excess (e.g., most and more), scarcity (e.g., less and few) or negation (e.g., not), we double the sentiment scores, halve the scores or swap the positive and negative scores, respectively.

## N-gram Generation
Next, we generate a list of all N-grams on top of the pre-processed text of the review. We experiments with unigrams, bigrams and trigrams, and found that unigrams and bigrams tend to give the best results. The generated N-grams are lemmatized, marked for sentence start and end, and only contain nouns, adjectives, adverbs, verbs and interjections, where a word bears a meaning. Additionally, Each N-gram is given sentiment scores that are the average positive and negative sentiment scores of all the words in it.

## Training the classifiers
Next, we build the machine-learning classifiers that predict review ratings, one classifier for each domain. The classification unit is a single review, and the output is a positive integer between 1 and 5. Below are the list of features we use in the training process.

1. N-grams: The existence of the unigram, bigrams and trigrams
2. Average positive Sentiment Score: This is the average positive score of all the words in the underlying review.
3. Average Negative Sentiment Score: This is the average negative score of all the words in the underlying review.
4. Number of words (those with relevant POS tags)


## Tuning the classifiers
In order to reach the best results, we run a comprehensive tuning process on the development set where several parameters are examined. These parameters are:

1. Classifier Type: (1) Naive Bayes (2) SVMs (3) Random Forest (4) J48 Decision Tree (5) k-nearest neighbors
2. N-gram representation: (1) Count (number of  occurrences of an N-gram in the underlying review) (2) Boolean (whether the N-gram exists in the underlying review or not).
3. N-gram Degree: (1) One (2) One and Two (3) One, Two and Three
4. Size of N-grams: (1) 100  (2) 200  (3) 300  (4) 400  (5) 500  (This is the top N-grams for each value of N in the training data)

SVMs with unigram and bigram features give the best results in all domains. However, Health and Shopping classification is better when considering the top 400 N-grams in the training data, as opposed to 200 in the case of Restaurant. Additionally, the Count N-gram representation gives the best results in Health and Restaurant as opposed to the Boolean representation in Shopping.


## Tagging the Relevant Text
In addition to predicting the overall rating of a review, the system highlights the text whose existence affects the classification decision (the overall rating). This is done by inspecting the highly weighted N-grams in the classification model, as these N-grams have the biggest effect on the classification decision. Additionally, we apply thresholds (0.2 for bigrams and 0.3 for unigrams) for the associted positive/negative sentiment scores. In the case where highly-weighted N-grams intersect, the union of their corresponding text is selected.

## Processing an Input Review
Below are the processing steps applied on an input review.

1. Pre-processing the  review by doing white-space tokenization, sentence splitting, lemmatization, POS tagging and sentiment generation.
2. Generating the N-grams of the input review.
3. Run the best classification model on the input review to predict its overall rating.
4. Searching for the highly weighted N-grams in order to highlighting the relevant segments that affect the overall rating.
All the models and the engine that does the classification and the tagging of the relevant text are deployed on Bluemix, while the front-end application is deployed separately and calls the Bluemix service directly.

# Results and Analysis

## Results
The table below shows the performance of the system in the different domains compared to two baselines: 

- a majority baseline, where a rating of five is always assigned  
- a unigram baseline, where only unigrams are considered without any sort of sophisticated processing or sentiment analysis.

The accuracy of the system is 73.5%, 53.0%, 48.7% and 58.4% in Health, Shopping, Restaurant and overall, respectively, where the system consistently outperforms the two baselines. The overall system accuracy significantly increases to 83.1% when evaluating with a tolerance of one.

| Domain     | Majority Baseline | Unigram Baseline | System |
|------------|-------------------|------------------|--------|
| Health     | 59.7%             | 64.6%            | 73.5%  |
| Shopping   | 40.7%             | 43.8%            | 53.0%  |
| Restaurant | 35.5%             | 41.4%            | 48.7%  |
| Overall    | 45.3%             | 49.9%            | 58.4%  |

## Analysis
We conduct an analysis where we find that the system is very effective in predicting the outliers (ratings of five and one) at a high accuracy of about 81.2%. However, the accuracy of the system decreases when the rating ranges between two and four. By inspecting the data, we find that it is frequent that the text of a review does not make enough distinction that allows predicting the overall rating at a good accuracy.

When we compare the three domains: Health, Shopping and Restaurant, we find that people tend to give outlier ratings in the majority of their reviews in Health, while the percentage of the outliers goes down in Shopping and significantly decreases in Restaurant, which elaborates the reason why the system performs best in Health.

## Comparison to similar systems
The system outperforms the systems described above. However, the systems we compare with are designated for the rating of other domains. Thus, the comparison does not involve a direct link between the different systems. Our system outperforms the AT&T system that does rating prediction for products (See the Related Work section) by absolute 3%. We also outperform the Stanford system for movie rating by absolute 6%.

# Overall System

We decided to put the application on the web. For this we designed a backend system running on Bluemix with a GET request endpoint that takes in a review with a domain and returns an analysis. The analysis is a predicted rating along with the review text where the significant words are highlighted. This backend java web service was hosted at

http://rrs-v7.mybluemix.net.

Example request:    http://rrs-v7.mybluemix.net/analyze?review=good&domain=health
Returns: { "text": "[[good]]", "rating": "5"}
Note that the double brackets are highlights.

We set a front-end system to be able to use the service more freely. The webapp takes as input either a URL of a yelp business from which it will extract eight reviews or takes in a single review text with its domain. It returns the review text with highlighted words, the predicted rating and it’s real rating.

Currently it works with these three business urls because the Yelp API doesn’t allow for you to get reviews of a business and we didn’t have enough time to write a web crawler:

Restaurant: http://www.yelp.com/biz/dig-inn-seasonal-market-new-york-18
Shopping: http://www.yelp.com/biz/book-culture-new-york
Health: http://www.yelp.com/biz/columbia-university-dental-associates-new-york

Rating a single review works with any text!

The webapp is currently hosted on Bluemix at http://helloworld-html-rrs.mybluemix.net/

# Conclusion

Our system improves the state of the art in rating prediction for Yelp reviews and provides the additional justification module that make it useful for businesses and customers. The ambiguity in the ratings, especially those between two and four stars that are more nuanced and are even hard to distinguish by humans makes increasing accuracy beyond 60% a difficult task. However, we believe our system provides good overall performance and tags meaningful passages in reviews.