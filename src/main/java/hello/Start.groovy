
//
//def number = 5
//number.times {
//    d -> print(d)
//}

def list = [1,2,3,5,6,7];
def rs = list.findAll {
    x -> x > 5
}

def con = { x, y ,z -> x + y + z }


print con(10,20,30);

