package dan.nr.sample.util

object InputValidator
{
    fun validateInputs(arg1:String, arg2:String): Boolean
    {
        return (arg1.isNotEmpty() && arg2.isNotEmpty())
    }
}