public class ThreeSumDistinct{

	public static void main(String[] args){
		int[] nums = {1, 2, -5, 3, 10, -11};
		System.out.print(threesumdistinct(nums));
	}

	public static boolean threesumdistinct(int[] nums){
		for (int i = 0; i < nums.length - 2; i += 1){
			for (int j = i + 1; j < nums.length - 1; j += 1){
				for (int k = j + 1; k < nums.length; k += 1){
					if (nums[i] + nums[j] + nums[k] == 0) return true;
				}
			}
		}
	return false;
	}

}