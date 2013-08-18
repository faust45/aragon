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
        var data = eval("(" + data + ")");

        $('.yellow').removeClass('yellow');

        for(var i in data) {
            var coll = data[i];
            //if(coll[0]) {
            //console.log(coll, coll[0]);
            //var start = $('[data-square="'+coll[0]+'"]');
            //var end = $('[data-square="'+coll[coll.length - 1]+'"]');
            //console.log(start.position(), start, end);
            // 
            //var fromPoint = {},
            //    toPoint = {};

            //fromPoint.x = start.offset().left;
            //fromPoint.y = start.offset().top;
            //toPoint.x = end.offset().left;
            //toPoint.y = end.offset().top;
            //$.line(fromPoint, toPoint); 
            //}

            for(var i in coll) {
                $('[data-square='+coll[i]+']').addClass('yellow');
            }
        }
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
