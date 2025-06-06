package com.zjgsu.cook


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.cook.data.FoodItemEntity
import com.zjgsu.cook.data.toEntity
import com.zjgsu.cook.databinding.FragmentFoodManagementBinding
class FoodManagementFragment : Fragment() {

    private var _binding: FragmentFoodManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItems = mutableListOf<FoodItem>()

    private lateinit var foodViewModel: FoodViewModel
    var userId: Int = 0 // 假设你有当前登录用户的 id

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化 ViewModel
        foodViewModel = FoodViewModel(requireActivity().application)

        // TODO: 这里要从SharedPreferences或者其他地方获取当前登录用户ID
        val prefs = requireContext().getSharedPreferences("user_prefs", 0)
        userId = prefs.getInt("user_id", 0)
        if (userId == 0) {
            // 说明没登录，或者没有正确保存
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            // 你可以跳转登录页或者做别的处理
        }
        val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putInt("user_id", userId)?.apply()

        setupRecyclerView()
        observeAllFoodLists()
        setupTabClicks()


        foodViewModel.foodItems.observe(viewLifecycleOwner) { list ->
            val foodItems = list.map { entity -> entity.toFoodItem() }
            foodItemAdapter.updateData(foodItems)
        }
        foodItemAdapter.onDeleteListener = { item ->
            foodViewModel.deleteFoodItem(item)  // ViewModel 中实现真正删除逻辑
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            foodViewModel.loadFoodItems()
        }

        foodItemAdapter.onDeleteListener = { foodItem ->
            val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getInt("user_id", -1)
            if (userId != -1) {
                val foodEntity = foodItem.toEntity(userId)
                foodViewModel.deleteFood(foodEntity)
            }
        }

        binding.buttonAddFood.setOnClickListener {
            if (childFragmentManager.findFragmentByTag("addFoodDialog") == null) {
                val dialog = AddFoodDialogFragment()
                dialog.setOnFoodAddedListener { newItem ->
                    // 添加到数据库
                    addFoodItemToDatabase(newItem)
                }
                dialog.show(childFragmentManager, "addFoodDialog")
            }
        }

        // 加载当前用户的食材数据
        foodViewModel.loadFoodItems()

        binding.recyclerViewFood.adapter = foodItemAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // 不支持拖拽
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                foodItemAdapter.swipeToDelete(position)
            }





            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // 可选，打印日志确认是否允许滑动
                Log.d("ItemTouchHelper", "getSwipeDirs for position=${viewHolder.bindingAdapterPosition}")
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewFood)


    }

    private fun setupTabClicks() {
        binding.tabRecent.setOnClickListener {
            currentTab = Tab.RECENT
            observeAllFoodLists()
        }

        binding.tabExpiring.setOnClickListener {
            currentTab = Tab.NEAR_EXPIRY
            observeAllFoodLists()
        }

        binding.tabExpired.setOnClickListener {
            currentTab = Tab.EXPIRED
            observeAllFoodLists()
        }
    }


    private fun setupRecyclerView() {
        foodItemAdapter = FoodItemAdapter(foodItems) { foodItem ->
            openFoodDetail(foodItem)
        }
        binding.recyclerViewFood.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFood.adapter = foodItemAdapter
    }

    private fun observeAllFoodLists() {
        foodViewModel.recentFoods.observe(viewLifecycleOwner) { recentList ->
            if (currentTab == Tab.RECENT) {
                foodItems.clear()
                foodItems.addAll(recentList.map { it.toFoodItem() })
                foodItemAdapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        foodViewModel.nearExpiryFoods.observe(viewLifecycleOwner) { nearList ->
            if (currentTab == Tab.NEAR_EXPIRY) {
                foodItems.clear()
                foodItems.addAll(nearList.map { it.toFoodItem() })
                foodItemAdapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        foodViewModel.expiredFoods.observe(viewLifecycleOwner) { expiredList ->
            if (currentTab == Tab.EXPIRED) {
                foodItems.clear()
                foodItems.addAll(expiredList.map { it.toFoodItem() })
                foodItemAdapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    private enum class Tab {
        RECENT, NEAR_EXPIRY, EXPIRED
    }
    private var currentTab: Tab = Tab.RECENT


    private fun getCurrentUserId(): Int {
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        return prefs.getInt("user_id", -1) // -1 表示未登录或不存在
    }

    private fun addFoodItemToDatabase(foodItem: FoodItem) {
        // 构造数据库用的 FoodItemEntity，确保 userId 正确
        val userId = getCurrentUserId()
        if (userId == -1) {
            // 用户未登录，提示或处理
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        val entity = FoodItemEntity(
            id = 0,
            name = foodItem.name,
            quantity = foodItem.quantity,
            expiryDate = foodItem.expiryDate.toString(),
            userId = userId
        )

        foodViewModel.addFood(entity)
    }

    private fun openFoodDetail(foodItem: FoodItem) {
        val intent = Intent(requireContext(), FoodDetailActivity::class.java)
        intent.putExtra("foodItem", foodItem)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


