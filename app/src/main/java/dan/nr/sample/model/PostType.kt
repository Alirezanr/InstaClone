package dan.nr.sample.model

enum class PostType(val value: Int)
{
    ONLY_TEXT(0),
    IMAGE_AND_TEXT(1),
    VIDEO_AND_TEXT(2);
}