# 📘 React Quick Notes — Chapter 6: Data Fetching & Side Effects ☁️  
> *“From naive fetch to production-grade data synchronization.”*

## 🗂️ Table of Contents

1.  [🌐 Fetching Data with `useEffect` (The Naive Way)](#-1-fetching-data-with-useeffect-the-naive-way)
2.  [🌀 Handling Loading, Error, and Success States](#-2-handling-loading-error-and-success-states)
3.  [🚫 The Problems with `useEffect` for Data Fetching](#-3-the-problems-with-useeffect-for-data-fetching)
4.  [⚡ Introducing React Query (TanStack Query) v5](#-4-introducing-react-query-tanstack-query-v5)
5.  [🔄 Mutations: Creating, Updating, Deleting Data](#-5-mutations-creating-updating-deleting-data)
6.  [🧩 Advanced: Query Invalidation, Pagination, Prefetching](#-6-advanced-query-invalidation-pagination-prefetching)



## 🌐 1. Fetching Data with `useEffect` (The Naive Way)

### 💡 Concept
The most basic way to fetch data in React. It works, but has significant drawbacks.

### 🎯 Real-World Interview Example
> *“Fetch a list of posts from JSONPlaceholder when the component mounts.”*

```jsx
import { useState, useEffect } from 'react';

function PostList() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('https://jsonplaceholder.typicode.com/posts')
      .then(res => res.json())
      .then(data => {
        setPosts(data);
        setLoading(false);
      });
  }, []); // Empty deps = run once on mount

  if (loading) return <div>Loading posts... ⏳</div>;

  return (
    <ul>
      {posts.map(post => (
        <li key={post.id}>{post.title}</li>
      ))}
    </ul>
  );
}
```

> 💬 **Interview Tip**: “This is the starting point, but it’s naive. It lacks caching, automatic retries, pagination support, and can lead to race conditions. It’s fine for learning, but not for production apps.”

---

## 🌀 2. Handling Loading, Error, and Success States

### 💡 Concept
Always design for three states: **Loading**, **Error**, and **Success**. This is non-negotiable for UX.

### 🎯 Real-World Interview Example
> *“Enhance the PostList to handle network errors and display a retry button.”*

```jsx
import { useState, useEffect } from 'react';

function PostList() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchPosts = () => {
    setLoading(true);
    setError(null); // Clear previous error

    fetch('https://jsonplaceholder.typicode.com/posts')
      .then(res => {
        if (!res.ok) throw new Error('Network response was not ok');
        return res.json();
      })
      .then(data => {
        setPosts(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  };

  // Fetch on mount
  useEffect(() => {
    fetchPosts();
  }, []);

  if (loading) return <div>Loading posts... ⏳</div>;

  if (error) {
    return (
      <div>
        <p>Error: {error} ❌</p>
        <button onClick={fetchPosts}>Retry 🔄</button>
      </div>
    );
  }

  return (
    <ul>
      {posts.map(post => (
        <li key={post.id}>{post.title}</li>
      ))}
    </ul>
  );
}
```

> 💬 **Interview Tip**: “I always structure my data-fetching components around these three states. It’s critical for user trust. I also include a retry mechanism for transient network errors.”

---

## 🚫 3. The Problems with `useEffect` for Data Fetching

Here’s why `useEffect` alone is insufficient for serious apps:

| Problem                  | Description                                                                 | Real-World Impact                          |
|--------------------------|-----------------------------------------------------------------------------|--------------------------------------------|
| **No Caching**           | Data is refetched every time component mounts or re-renders.                | Wasted bandwidth, slower UX.               |
| **No Deduping**          | If two components fetch the same data, it’s fetched twice.                  | Redundant network requests.                |
| **No Background Updates**| Data becomes stale. User must refresh to see updates.                       | Users see outdated info.                   |
| **No Pagination Support**| Manual state management for pages, cursors, etc.                            | Complex, error-prone code.                 |
| **Race Conditions**      | If user clicks rapidly, responses might return out of order.                | UI shows wrong data.                       |
| **No Retry/Refetch**     | Manual implementation required for retrying failed requests.                | Poor resilience to network issues.         |

> 💬 **Interview Answer**:  
> “While `useEffect` is the foundation, it’s a low-level tool. For anything beyond a demo, I reach for a data-fetching library like React Query. It solves caching, deduping, background updates, pagination, and race conditions out of the box. It lets me focus on the UI, not the plumbing.”

---

## ⚡ 4. Introducing React Query (TanStack Query) v5

### 💡 Concept
A powerful library for managing server state. It provides hooks for fetching, caching, synchronizing, and updating server state.

### 🎯 Real-World Interview Example
> *“Refactor the PostList to use React Query. Show how caching prevents refetching.”*

```bash
npm install @tanstack/react-query
```

```jsx
// 1. Setup QueryClientProvider at the root (App.jsx)
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <PostList />
    </QueryClientProvider>
  );
}

// 2. Use useQuery in your component
import { useQuery } from '@tanstack/react-query';

function PostList() {
  const { data: posts, isLoading, isError, error, refetch } = useQuery({
    queryKey: ['posts'], // 🔑 Unique key for caching
    queryFn: async () => {
      const res = await fetch('https://jsonplaceholder.typicode.com/posts');
      if (!res.ok) throw new Error('Network error');
      return res.json();
    },
  });

  if (isLoading) return <div>Loading posts... ⏳</div>;

  if (isError) {
    return (
      <div>
        <p>Error: {error.message} ❌</p>
        <button onClick={refetch}>Retry 🔄</button>
      </div>
    );
  }

  return (
    <ul>
      {posts.map(post => (
        <li key={post.id}>{post.title}</li>
      ))}
    </ul>
  );
}
```

✅ **Magic**: If you navigate away and back, or if another component requests `['posts']`, it will use the **cached data** instantly, then optionally refetch in the background.

> 💬 **Interview Tip**: “React Query is a game-changer. The `queryKey` is how it caches and dedupes requests. `isLoading` and `isError` make state handling declarative. The `refetch` function is built-in for retries. This is my default for any data-fetching app.”

---

## 🔄 5. Mutations: Creating, Updating, Deleting Data

### 💡 Concept
`useMutation` is used for modifying server data (POST, PUT, DELETE). It automatically invalidates and refetches related queries.

### 🎯 Real-World Interview Example
> *“Add a button to ‘like’ a post. When clicked, send a POST request and refetch the post list.”*

```jsx
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

// Component for a single post
function PostItem({ post }) {
  const queryClient = useQueryClient(); // 🧠 Get the client to invalidate queries

  // Define the mutation
  const { mutate: likePost, isPending: isLiking } = useMutation({
    mutationFn: async (postId) => {
      const res = await fetch(`https://jsonplaceholder.typicode.com/posts/${postId}/like`, {
        method: 'POST',
        // ... add headers, body as needed
      });
      if (!res.ok) throw new Error('Failed to like post');
      return res.json();
    },
    onSuccess: () => {
      // ✅ Invalidate and refetch the 'posts' query
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });

  return (
    <li>
      {post.title}
      <button
        onClick={() => likePost(post.id)}
        disabled={isLiking}
        style={{ marginLeft: '10px' }}
      >
        {isLiking ? 'Liking...' : '👍 Like'}
      </button>
    </li>
  );
}

// Updated PostList
function PostList() {
  const { data: posts, isLoading, isError, error, refetch } = useQuery({
    queryKey: ['posts'],
    queryFn: async () => {
      const res = await fetch('https://jsonplaceholder.typicode.com/posts');
      if (!res.ok) throw new Error('Network error');
      return res.json();
    },
  });

  if (isLoading) return <div>Loading posts... ⏳</div>;
  if (isError) return <div>Error: {error.message} ❌ <button onClick={refetch}>Retry</button></div>;

  return (
    <ul>
      {posts.map(post => (
        <PostItem key={post.id} post={post} />
      ))}
    </ul>
  );
}
```

> 💬 **Interview Tip**: “`useMutation` handles the loading state (`isPending`) and lets you define `onSuccess` to invalidate related queries. This ensures the UI stays in sync with the server automatically. No more manual `setPosts([...posts, newPost])`!”

---

## 🧩 6. Advanced: Query Invalidation, Pagination, Prefetching

### 💡 Concept
React Query’s superpowers for complex scenarios.

### 🎯 Real-World Interview Example
> *“Implement pagination for the post list. Also, prefetch the next page when the user hovers over a ‘Next’ button.”*

```jsx
import { useQuery, useQueryClient } from '@tanstack/react-query';

function PaginatedPostList() {
  const [page, setPage] = useState(1);
  const queryClient = useQueryClient();

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['posts', page], // 🔢 Include page in key
    queryFn: async () => {
      const res = await fetch(`https://jsonplaceholder.typicode.com/posts?_page=${page}&_limit=10`);
      if (!res.ok) throw new Error('Network error');
      return res.json();
    },
  });

  // 🚀 Prefetch next page on hover
  const handleMouseEnter = () => {
    if (data && data.length > 0) { // Only if current page has data
      queryClient.prefetchQuery({
        queryKey: ['posts', page + 1],
        queryFn: async () => {
          const res = await fetch(`https://jsonplaceholder.typicode.com/posts?_page=${page + 1}&_limit=10`);
          return res.json();
        },
      });
    }
  };

  if (isLoading) return <div>Loading page {page}... ⏳</div>;
  if (isError) return <div>Error: {error.message} ❌</div>;

  return (
    <div>
      <ul>
        {data.map(post => (
          <li key={post.id}>{post.title}</li>
        ))}
      </ul>
      <div>
        <button
          onClick={() => setPage(p => Math.max(1, p - 1))}
          disabled={page === 1}
        >
          Previous ◀️
        </button>
        <span> Page {page} </span>
        <button
          onClick={() => setPage(p => p + 1)}
          onMouseEnter={handleMouseEnter} // 👈 Prefetch on hover!
        >
          Next ▶️
        </button>
      </div>
    </div>
  );
}
```

> 💬 **Interview Tip**: “Pagination is handled by including the page number in the `queryKey`. Prefetching is a huge UX win — loading data before the user even clicks. React Query’s `prefetchQuery` makes this trivial. This is the kind of polish that impresses interviewers.”

