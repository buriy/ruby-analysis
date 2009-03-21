def trace(event, file, line, id, binding, klass)            
  l = File.open(file, "r").readlines[line - 1]    
  puts "#{event} #{file}:#{line} #{l}"
end

set_trace_func proc { |event, file, line, id, binding, klass, *rest|
  trace(event, file, line, id, binding, klass)
}

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
puts "foo"
z = bar(y)
