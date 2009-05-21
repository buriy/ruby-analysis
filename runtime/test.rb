require "/Users/fourdman/bar.rb"

z = $xarklajskfljasdlk
@@y = 45

class Foo
  Bar = 33
end

def bar(y)
  x = 2
  puts "bar!"
  puts $x
  puts @x
  puts @@y
  puts Foo::Bar
  x.to_i
  44
end

y = 9
x = Foo
puts x
x2 = Foo.new
puts x2
puts "foo"
z = bar(y)