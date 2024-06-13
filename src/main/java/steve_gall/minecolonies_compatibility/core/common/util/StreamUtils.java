package steve_gall.minecolonies_compatibility.core.common.util;

import java.util.stream.Stream;

public class StreamUtils
{
	public static <T> Iterable<T> toIterable(Stream<T> stream)
	{
		return stream::iterator;
	}

	private StreamUtils()
	{

	}

}
