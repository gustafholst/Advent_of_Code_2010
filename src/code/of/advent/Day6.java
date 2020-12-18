package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Day6 {
	
	private static class Group {
		
		Set<Character> answers = new HashSet<>();
		
		private void addAnswer(char a) {
			answers.add(a);
		}
		
		public int getGroupCount() {
			return answers.size();
		}
		
		public static Group parseList(List<String> list) {
			Group g = new Group();
			
			/* part 1 */
//			list.stream().flatMap((String s) -> s.chars().mapToObj(c -> (char)c))
//			.distinct()
//			.forEach(a -> g.addAnswer(a));
			
			/* part 2 */
			List<Character> answeredByAll = new ArrayList<>();
			
			//initialize answered by all with the answers of first person in group
			list.stream().limit(1)
			.flatMap((String s) -> s.chars().mapToObj(c -> (char)c))
			.forEach(answeredByAll::add);
			
			//remove any answers from the list that aren't answered by others
			list.stream().skip(1)
			.forEach(s -> {
				Iterator<Character> it = answeredByAll.iterator();
				while (it.hasNext()) {
					if (s.indexOf(it.next()) < 0) {
						it.remove();
					}
				}	
			});
			
			answeredByAll.forEach(g::addAnswer);
			
			/* end part 2*/
			
			return g;
		}
	}

	public static <T> Stream<List<T>> buffer(Stream<T> stream) {
	    final Iterator<T> streamIterator = stream.iterator();

	    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<List<T>>() {
	        @Override
	        public boolean hasNext() {
	            return streamIterator.hasNext();
	        }

	        @Override
	        public List<T> next() {
	            if (!hasNext()) {
	                throw new NoSuchElementException();
	            }
	            List<T> intermediate = new ArrayList<>();
	            while (hasNext()) {
	            	T next = streamIterator.next();
	            	if (next.equals(""))
	            		break;
	                intermediate.add(next);
	            }
	            return intermediate;
	        }
	    }, 0), false);
	}
	
	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("answers.txt"));
			
			List<List<String>> groupsRaw = buffer(br.lines()).collect(Collectors.toList());
			
			System.out.println(groupsRaw.size());
			
			List<Group> groups = groupsRaw.stream().map(Group::parseList).collect(Collectors.toList());
			
			long totalCount = groups.stream().map(Group::getGroupCount).mapToInt(Integer::valueOf).sum();
			
			System.out.println("Total count: " + totalCount);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
