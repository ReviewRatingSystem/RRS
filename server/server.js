var express = require('express');
var app = express();

var bodyParser = require('body-parser');
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

var local_port = 3001;
var local_host = 'localhost'

app.get('/', function(req, res) {
  var instructions = {
    'GET /everything': 'sugar',
    'POST /getReviews': 'spice'
  }
  res.send(instructions);
});

app.post('/getReviews', function(req, res) {
    var business_url = req.body.business_url;
    console.log(business_url)
    res.send(business_url);
});

var server = app.listen(local_port, local_host, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log('API listening at http://%s:%s', host, port);
});
