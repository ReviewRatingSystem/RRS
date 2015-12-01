$(document).ready(function () {
  var form_url = $('.form-url')
  var form_review = $('.form-review')

  form_url.on("submit", function(event) {
    event.preventDefault()
    var url_text = $('#inputUrl').val();
    console.log(url_text)
  });

  form_review.on("submit", function(event) {
    event.preventDefault()
    var review_text = $('#inputReview').val();
    console.log(review_text)
  });

})
