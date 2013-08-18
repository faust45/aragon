Board = {
}

// 'w' 'pa1 ke1 kne4'
//

Board.getPosition = function() {
  var pos = $('#board').find('img').map(function() {
      var td = $($(this).parent()),
          figName  = $(this).data('name'),
          color    = $(this).data('color'),
          xy       = td.data('square') + '';
          position = [parseInt(xy.charAt(0)), parseInt(xy.charAt(1))];

          color = (color == "d" ? "black" : "white");

      return [[position, color, figName]];
  });

  return pos.toArray();
}

Board.render = function() {
    var source   = $("#board-template").html();
    var template = Handlebars.compile(source);

    $('#board').append(template({}));
}

Board.setPosition = function(color, figures) {
  var positions = $.map(figures.split(' '), function(pos) {
      return [pos.match(/(K|Q|R|B|N|P)(\w)(\d)/i)];
  });

  $.each(positions, function(i, pos) {
     pos.unshift(color);
     renderFigure.apply(this, pos);
  });

  function renderFigure(color, _, fig, col, row) {
    var path = "";
    path += (color == "w") ? 'l' : 'd';
    path += 't45.svg' 

    var img = $('<img style="position: relative;" class="ui-draggable" />');
    img.attr('data-piece', fig);
    img.attr('data-color', color);
    img.attr('src', 'images/wikipedia/Chess_' + path.toLowerCase());

    $('#board td[data-square='+ col + row +']').html(img);
  }
}

Board.init = function() {
    this.render();

    var undoStack = [];
    var redoStack = [];
    var dragSettings = {revert: true,
                        revertDuration: 0,
                        zIndex: 1};

    var square = function(name) {
        return $('[data-square=' + name + ']');
    };

    var pieceOn = function(name) {
        return $('[data-square=' + name + '] img');
    };

    var extra = function(name) {
        return $('.extra [data-piece=' + name + ']');
    };

    var undoButton = $('#undo');
    var redoButton = $('#redo');

    //Disable buttons since refreshing page sometimes retains their state.
    undoButton.prop('disabled', true);
    redoButton.prop('disabled', true);

    $('.board img').livequery(function() {
        $(this).draggable(dragSettings);
    });

    $('.figs img').livequery(function() {
        $(this).draggable(dragSettings);
    });

    $('.board td').droppable({
        drop: function(event, ui) {
            var $this = $(this);
            var draggable = ui.draggable;

            var toPlace = draggable.clone().attr('style', '');
            $this.append(toPlace);
            toPlace.draggable(dragSettings);

            if (draggable.parents('#board').get(0)) {
               draggable.remove(); 
            }

        }
    });

    $('img.trash').droppable({
        drop: function(event, ui) {
        }
    });

    undoButton.on('click', function() {
        var undoEntry = undoStack.pop();
        undoEntry.forEach(function (item) {
            var piece = item.piece;
            var to    = item.to;
            var from  = item.from;

            var pieceElement = (to !== null ?
                                pieceOn(to) :
                                extra(piece).clone().draggable(dragSettings));

            if (from !== null) {
                pieceElement.appendTo(square(from));
            }
            else {
                pieceElement.remove();
            }
        });
        redoStack.push(undoEntry);
        redoButton.prop('disabled', false);

        if (undoStack.length === 0) {
            undoButton.prop('disabled', true);
        }
    });

    redoButton.on('click', function() {
        var moves = [];

        var redoEntry = redoStack.pop();
        redoEntry.forEach(function(item) {
            moves.push({
                piece : pieceOn(item.from),
                to    : square(item.to)
            });
        });

        moves.forEach(function(item) {
            var to    = item.to;
            var piece = item.piece;

            if (to.length > 0) {
                piece.appendTo(to);
            }
            else {
                piece.remove();
            }
        });

        undoStack.push(redoEntry);
        undoButton.prop('disabled', false);

        if (redoStack.length === 0) {
            redoButton.prop('disabled', true);
        }
    });
};
