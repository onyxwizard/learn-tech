// Garbage Collection;

//Interlinked objects
/*
+-------------------+
| <global variable> |
+-------------------+
          |
          v
        family
          |
          v
       +-------+
       | Object|
       +-------+
         /     \
        /       \
   father      mother
      |           |
      v           v
+----------+     +----------+
| Object  |<---->| Object  |
| name: "John" | | name: "Ann"|
+----------+     +----------+
         \       /
          \     /
           \   /
            \ /
             v
          husband
*/
function marry(man, woman) {
	woman.husband = man;
	man.wife = woman;

	return {
		father: man,
		mother: woman,
	};
}

let family = marry(
	{
		name: "John",
	},
	{
		name: "Ann",
	}
);

// Now lets delete the link and clear from memory
delete family.father;
delete family.mother.husband;
/*
Before deletion:                          After deletion:
(delete family.father)                    (delete family.mother.husband)
+-------------------+                     +-------------------+
| <global variable> |                     | <global variable> |
+-------------------+                     +-------------------+
          |                                       |
          v                                       v
      family                                  family
          |                                       |
          v                                       v
       +-------+                               +-------+
       | Object|                               | Object|
       +-------+                               +-------+
         \     /                                 \     /
          \   /                                   \   /
           \ /                                     \ /
            v                                       v
         mother                                  mother
            |                                       |
            v                                       v
+----------------+  wife    +-----------+       +-----------+
| Object         |------>  | Object     |       | Object     |
| name: "John"  | <------ | name: "Ann"|       | name: "Ann"|
+---------------+ Husband +------------+       +------------+
*/
