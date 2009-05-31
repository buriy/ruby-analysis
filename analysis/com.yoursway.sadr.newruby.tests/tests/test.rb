require 'socket'

class Foo
	def ggg
	end
	
	def self.Goo
	end
end

def bar(x, y)
	z = x + y
	return z
end

puts bar(4, bar(3, 3))