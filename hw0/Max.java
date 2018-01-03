public class Max{

	public static void main(String[] args){
		int[] nums = {1, 3, 5, 9, 10, 5};
		System.out.print(max(nums));
	}

	public static int max(int[] nums){
		int k = nums[0];
		// if (a.length == 0) return {};
		for (int i = 1; i < nums.length; i += 1){
			if (k < nums[i]) k = nums[i];
		}
		return k;
	}
}

