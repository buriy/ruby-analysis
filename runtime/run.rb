def trace(event, file, line, id, binding, klass)            
  l = File.open(file, "r").readlines[line - 1]    
  puts "#{event} #{file}:#{line} #{l}"
end

set_trace_func proc { |event, file, line, id, binding, klass, *rest|
  trace(event, file, line, id, binding, klass)
}

def bar()
  x = 2
  puts "bar!"
end

puts "foo"
bar()