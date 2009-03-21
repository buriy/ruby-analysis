require 'socket'

class ParsingClient
  
  def initialize
    puts "boom!"
    @sock = TCPSocket.new("localhost", 6789)
  end
  
  def items_at(file, line)
    @sock << "evaluatable_items #{file} #{line}\n"
    result = []
    while (true) 
      l = @sock.readline
      if l =~ /^error.*$/
        return nil
      end
      if l =~ /^done.*$/
        return result
      end
      s = l.split
      result << { "name" => s[0], "type" => s[1] }
    end
    result
  end
  
end

$parsing = ParsingClient.new()

def trace(event, file, line, id, binding, klass)            
  l = File.open(file, "r").readlines[line - 1]  
  file = File.expand_path(file)
  return if (event != "line")
  puts "#{event} #{file}:#{line} #{l}"
  
  items = $parsing.items_at(file, line - 1)
  if items
    p items
    result = []
    items.each { |i|
      begin 
        v = eval(i["name"], binding).class.to_s
      rescue Object => e
        v = "<error:#{e}>"
      end
      result << { "name" => i["name"], "type" => v}
    }
    p result
  else
    puts "- failed to fetch evaluatable items"
  end
end

set_trace_func proc { |event, file, line, id, binding, klass, *rest|
  trace(event, file, line, id, binding, klass)
}

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
puts "foo"
z = bar(y)
