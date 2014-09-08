class Main

def log(msg)
  puts "#{Time.now.strftime('%H:%M:%S.%L')}:#{msg}"
end

def go

log "boo"

jobs = ["manager", "clerk", "footballer", "artist", "teacher"]
colours = ["red", "blue", "green", "yellow", "orange"]

@database = []
(0..1000000).each do |i|
  @database << {"name" => "Mr #{i}", "colour" => colours[rand*colours.length], "job" => jobs[rand*jobs.length]}
end

log "Loaded objects:#{@database.length}:#{@database[0].inspect}"

(0..5).each do |i|
  find_colour("red")
end

end

def find_colour(colour)
  log "find_colour:start"
  start = Time.now
  filtered = []
  @database.each do |entry|
    filtered << entry if entry["colour"] == colour
  end
  elapsed = Time.now - start
  log "find_colour:end:#{filtered.length}:#{elapsed*1000}ms"
end

end

Main.new.go