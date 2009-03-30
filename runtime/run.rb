#!/usr/bin/ruby

require 'socket'

class ParsingClient
  
  def initialize
    @sock = TCPSocket.new("localhost", 6789)
    @files = {}
  end
  
  def items_at(file, line)
    file_result = @files[file]
    if not file_result
      @sock << "parse #{file}\n"
      file_result = {}
      result = []
      lno = 0
      while (true) 
        l = @sock.readline
        break if l =~ /^error.*$/
        break if l =~ /^done.*$/
        if l =~ /^\d+$/
          lno = l.to_i
          result = []
          file_result[lno] = result
          next
        end
        s = l.split
        result << { "name" => s[0], "type" => s[1] }
      end
      @files[file] = file_result
    end
    return file_result[line]
  end
  
end

$parsing = ParsingClient.new()
$tracing_stream = File.new("trace.log", "w")

def trace(event, file, line, id, binding, klass)            
  l = File.open(file, "r").readlines[line - 1]  
  file = File.expand_path(file)
  current_file = File.expand_path(__FILE__)
  return if (current_file == file)
  return if (event != "line")
  
  $tracing_stream.puts "#{event} #{line} #{file} \t\t\t#{l}"
  
  items = $parsing.items_at(file, line - 1)
  if items
    #$tracing_stream.puts items
    #result = []
    items.each { |i|
      begin 
        v = eval(i["name"], binding).class.to_s
      rescue Object => e
        v = "<error:#{e}>"
      end
      #result << { "name" => i["name"], "type" => v}
      $tracing_stream.puts "#{i["name"]} #{v}"
    }
    #$tracing_stream.puts result
  else
    $tracing_stream.puts "- failed to fetch evaluatable items"
  end
end

set_trace_func proc { |event, file, line, id, binding, klass, *rest|
  trace(event, file, line, id, binding, klass)
}

require ARGV[0]
