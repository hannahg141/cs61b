public class ThreeSum{

	public static void main(String[] args){
		int[] nums = {1, 2, -5, 3, 10, -11};
		System.out.print(threesum(nums));
	}

	public static boolean threesum(int[] nums){
		for (int i = 0; i < nums.length; i += 1){
			for (int j = 0; j < nums.length; j += 1){
				for (int k = 0; k < nums.length; k += 1){
					if (nums[i] + nums[j] + nums[k] == 0) return true;
				}
			}
		}
	return false;
	}

}