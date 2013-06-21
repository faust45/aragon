ll = function() {
    console.log.apply(this, arguments);
}

$(function() {
    Board.init();

    var source   = $("#fig-list-template").html();
    var template = Handlebars.compile(source);

    $('#figs').append(template({}));


    $('#sendPos').click(function() {
      var pos = Board.getPosition();
      sendPosition(pos);
    });


    //getPosition(function(position) {
    //  Board.setPosition('w', position.w);
    //  Board.setPosition('b', position.b);
    //});
})

function sendPosition(pos) {
  $.ajax({
      type: 'POST',
      url: '/analyzePosition',
      contentType: 'application/json',
      data: JSON.stringify(pos),
      success: function(data, status) {
        console.log(data);
      }
  });
}

function makeMove(fig, from, to) {
    var move = fig + " " + from + " " + to;

    $.ajax({
      type: 'POST',
      url: '/move',
      contentType: 'application/chess',
      data: move,
      success: function(data, status) {
        console.log(data);
      }
    });
}

function getPosition(fn) {
  $.get('/current_position', function(data) {
      console.log('in debug ', data);
      fn(data);
  });
}
