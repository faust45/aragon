
Handlebars.registerHelper("stripes", function(array, row, even, odd, fn, elseFn) { 

    if (array && array.length > 0) {
        var buffer = "";
        if ((row % 2) == 0) {
            var t = even;
            even = odd;
            odd = t;
        }

        for (var i = 0, j = array.length; i < j; i++) { 
            var item = {};
            
            item.stripeClass = (i % 2 == 0 ? even : odd);

            item.square = array[i] + row;
            buffer += fn.fn(item); 
        }

        return buffer; 
    } else { 
        return elseFn(); 
    } 
});

Handlebars.registerHelper("eachW", function(array, row, fn) { 
    if (array && array.length > 0) {
        var buffer = "";
        for (var i = 0, j = array.length; i < j; i++) {
            var item = {};
            
            item.first = array[i];
            item.second = row;
            buffer += fn.fn(item); 
        }

        return buffer; 
    }
});

function reverse(s){
    return s.split("").reverse().join("");
}
